package dev.lukman.maybank.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lukman.maybank.api.request.RequestParameterSearch;
import dev.lukman.maybank.dto.GithubResponse;
import dev.lukman.maybank.dto.ReportHistoryDTO;
import dev.lukman.maybank.exception.ErrorException;
import dev.lukman.maybank.repository.ReportHistoryRepository;
import dev.lukman.maybank.service.JasperService;
import dev.lukman.maybank.utils.GenerateRandomString;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.lukman.maybank.constant.GlobalConstant.*;
import static dev.lukman.maybank.constant.ResponseMessage.*;

@Service
@Transactional
@RequiredArgsConstructor
public class JasperServiceImpl implements JasperService {

    @Value("${do.space.bucket}")
    private String doSpaceBucket;
    private final AmazonS3 s3Client;
    private final ReportHistoryRepository reportHistoryRepository;

    @Override
    public byte[] generateReportPDF(GithubResponse githubResponse, RequestParameterSearch parameter) {
        try {
            InputStream jasperStream = this.getClass().getResourceAsStream(PATH_TEMPLATE_REPORT_GITHUB);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperStream);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(githubResponse.getItems());
            List<Object> dataList = objectMapper.readValue(json, new TypeReference<>() {});
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);

            int size = parameter.getSize() == 0 ? DEFAULT_SIZE_PAGE : parameter.getSize();
            int page = parameter.getPage() == 0 ? DEFAULT_PAGE : parameter.getPage();
            String pagination = page + "/" + size / githubResponse.getTotalCount();
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("Query", parameter.getSearch());
            parameters.put("Pagination", pagination);
            parameters.put("TotalRow", size);
            parameters.put("TotalCount", githubResponse.getTotalCount());

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            String fileId = saveReportIntoSpace(outputStream.toByteArray());
            int itemCount = githubResponse.getItems() != null ? githubResponse.getItems().size() : 0;

            reportHistoryRepository.save(new ReportHistoryDTO(null, fileId, parameter.getSearch(), itemCount, null).toModel());
            return outputStream.toByteArray();
        } catch (JRException | IOException e) {
            throw new ErrorException(HttpStatus.BAD_REQUEST.value(), RESPONSE_FAILED_GENERATE, HttpStatus.BAD_REQUEST);
        }
    }

    private String saveReportIntoSpace(byte[] pdfData) {
        String fileId = GenerateRandomString.generateSecureRandomString(100);
        String key = fileId + "/" + FILE_NAME;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(pdfData.length);
        metadata.setContentType("application/pdf");

        try {
            s3Client.putObject(new PutObjectRequest(doSpaceBucket, key, new ByteArrayInputStream(pdfData), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return fileId;
        } catch (AmazonServiceException e) {
            if (e.getErrorCode().equals("InvalidAccessKeyId")){
                throw new ErrorException(400, RESPONSE_SPACE_INVALID_KEY, HttpStatus.BAD_REQUEST);
            } else {
                throw new ErrorException(400, RESPONSE_SPACE_INVALID_SECRET, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new ErrorException(400, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public byte[] downloadReport(String fileId) {
        try {
            String key = fileId + "/" + FILE_NAME;
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(doSpaceBucket, key));

            byte[] content;
            try (S3ObjectInputStream s3is = s3Object.getObjectContent();
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = s3is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                content = baos.toByteArray();
            }
            return content;
        } catch (AmazonServiceException e) {
            throw new ErrorException(e.getStatusCode(), RESPONSE_FAILED_DOWNLOAD_REPORT, HttpStatus.valueOf(e.getStatusCode()));
        } catch (IOException e) {
            throw new ErrorException(400, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
