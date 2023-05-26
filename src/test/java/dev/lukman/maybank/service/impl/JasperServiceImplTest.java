package dev.lukman.maybank.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import dev.lukman.maybank.api.request.RequestParameterSearch;
import dev.lukman.maybank.dto.GithubResponse;
import dev.lukman.maybank.dto.GithubUserDTO;
import dev.lukman.maybank.exception.ErrorException;
import dev.lukman.maybank.repository.ReportHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JasperServiceImplTest {

    private JasperServiceImpl jasperService;

    @Mock
    private AmazonS3 s3Client;

    @Mock
    private ReportHistoryRepository reportHistoryRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jasperService = new JasperServiceImpl(s3Client, reportHistoryRepository);
    }

    @Test
    void testGenerateReportPDF_Success() {
        GithubResponse githubResponse = new GithubResponse();
        githubResponse.setTotalCount(1);
        githubResponse.setIncompleteResults(false);
        githubResponse.setItems(List.of(new GithubUserDTO()));

        RequestParameterSearch parameter = new RequestParameterSearch();
        parameter.setSearch("openai");
        parameter.setSort("joined");
        parameter.setOrder("desc");
        parameter.setPage(1);
        parameter.setSize(10);

        byte[] reportBytes = jasperService.generateReportPDF(githubResponse, parameter);

        assertNotNull(reportBytes);
        assertTrue(reportBytes.length > 0);
    }

    @Test
    void testGenerateReportPDF_Fail() {
        GithubResponse githubResponse = new GithubResponse();
        githubResponse.setTotalCount(1);
        githubResponse.setIncompleteResults(false);
        githubResponse.setItems(List.of(new GithubUserDTO()));

        RequestParameterSearch parameter = new RequestParameterSearch();
        parameter.setSearch("openai");
        parameter.setSort("joined");
        parameter.setOrder("desc");
        parameter.setPage(1);
        parameter.setSize(10);

        doThrow(RuntimeException.class).when(reportHistoryRepository).save(any());

        assertThrows(RuntimeException.class, () -> jasperService.generateReportPDF(githubResponse, parameter));
    }

    @Test
    void testDownloadReport_Success() {
        String fileId = "test-file-id";

        S3Object s3Object = mock(S3Object.class);
        S3ObjectInputStream s3is = new S3ObjectInputStream(new ByteArrayInputStream("test content".getBytes()), null);
        when(s3Object.getObjectContent()).thenReturn(s3is);

        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(s3Object);

        byte[] reportBytes = jasperService.downloadReport(fileId);

        assertNotNull(reportBytes);
        assertTrue(reportBytes.length > 0);
    }

    @Test
    void testDownloadReport_Fail() {
        String fileId = "test-file-id";

        AmazonServiceException exception = new AmazonServiceException("An error occurred");
        exception.setStatusCode(400);

        when(s3Client.getObject(any(GetObjectRequest.class))).thenThrow(exception);

        assertThrows(ErrorException.class, () -> jasperService.downloadReport(fileId));
    }
}
