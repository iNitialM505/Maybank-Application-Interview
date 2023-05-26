package dev.lukman.maybank.service.impl;

import dev.lukman.maybank.adapter.GithubClient;
import dev.lukman.maybank.api.request.RequestParameterSearch;
import dev.lukman.maybank.api.response.ResponseSuccess;
import dev.lukman.maybank.dto.GithubResponse;
import dev.lukman.maybank.dto.ReportHistoryDTO;
import dev.lukman.maybank.exception.ErrorException;
import dev.lukman.maybank.model.ReportHistory;
import dev.lukman.maybank.repository.ReportHistoryRepository;
import dev.lukman.maybank.service.JasperService;
import dev.lukman.maybank.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.lukman.maybank.constant.GlobalConstant.FILE_NAME;
import static dev.lukman.maybank.constant.ResponseMessage.RESPONSE_NO_RECORD;
import static dev.lukman.maybank.constant.ResponseMessage.RESPONSE_SUCCESS_FETCH;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final GithubClient githubClient;
    private final JasperService jasperService;
    private final ReportHistoryRepository reportHistoryRepository;

    @Override
    public ResponseEntity<Object> searchUser(RequestParameterSearch parameter) {
        GithubResponse githubResponse = githubClient.searchUsers(parameter);
        byte[] pdfBytes = jasperService.generateReportPDF(githubResponse, parameter);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(FILE_NAME).build());
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }

    @Override
    public ResponseEntity<Object> downloadReport(String id) {
        Optional<ReportHistory> reportHistoryOptional = reportHistoryRepository.findByFileId(id);
        if (reportHistoryOptional.isEmpty()) {
            throw new ErrorException(404, RESPONSE_NO_RECORD, HttpStatus.NOT_FOUND);
        }

        String fileId = reportHistoryOptional.get().getFileId();
        byte[] pdfBytes = jasperService.downloadReport(fileId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(FILE_NAME).build());
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> historyReport() {
        List<ReportHistoryDTO> reportHistoryDTOS = new ArrayList<>();
        List<ReportHistory> reportHistories = reportHistoryRepository.findAll();

        if (reportHistories.isEmpty()) {
            return ResponseEntity.ok(new ResponseSuccess()
                    .setMessage(RESPONSE_NO_RECORD)
                    .setData(reportHistoryDTOS));
        }

        reportHistories.forEach(reportHistory -> reportHistoryDTOS.add(new ReportHistoryDTO()
                .setId(reportHistory.getId())
                .setFileId(reportHistory.getFileId())
                .setParameter(reportHistory.getParameter())
                .setTotalData(reportHistory.getTotalData())
                .setCreatedAt(reportHistory.getCreatedAt())
        ));

        return ResponseEntity.ok(new ResponseSuccess()
                .setMessage(RESPONSE_SUCCESS_FETCH)
                .setData(reportHistoryDTOS));
    }
}
