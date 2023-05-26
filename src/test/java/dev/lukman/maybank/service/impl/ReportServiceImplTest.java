package dev.lukman.maybank.service.impl;

import dev.lukman.maybank.adapter.GithubClient;
import dev.lukman.maybank.api.request.RequestParameterSearch;
import dev.lukman.maybank.dto.GithubResponse;
import dev.lukman.maybank.dto.GithubUserDTO;
import dev.lukman.maybank.dto.ReportHistoryDTO;
import dev.lukman.maybank.model.ReportHistory;
import dev.lukman.maybank.repository.ReportHistoryRepository;
import dev.lukman.maybank.service.JasperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ReportServiceImplTest {

    private ReportServiceImpl reportService;

    @Mock
    private GithubClient githubClient;

    @Mock
    private JasperService jasperService;

    @Mock
    private ReportHistoryRepository reportHistoryRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reportService = new ReportServiceImpl(githubClient, jasperService, reportHistoryRepository);
    }

    @Test
    void testSearchUser_Success() {
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

        byte[] pdfBytes = new byte[10];

        when(githubClient.searchUsers(parameter)).thenReturn(githubResponse);
        when(jasperService.generateReportPDF(githubResponse, parameter)).thenReturn(pdfBytes);

        ResponseEntity<Object> response = reportService.searchUser(parameter);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(pdfBytes, (byte[]) response.getBody());
    }

    @Test
    void testDownloadReport_Success() {
        String fileId = "test-file-id";
        byte[] pdfBytes = new byte[10];
        ReportHistory reportHistory = new ReportHistory();
        reportHistory.setFileId(fileId);

        when(reportHistoryRepository.findByFileId(fileId)).thenReturn(Optional.of(reportHistory));
        when(jasperService.downloadReport(fileId)).thenReturn(pdfBytes);

        ResponseEntity<Object> response = reportService.downloadReport(fileId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(pdfBytes, (byte[]) response.getBody());
    }

    @Test
    void testHistoryReport_Success() {
        List<ReportHistory> reportHistories = new ArrayList<>();
        reportHistories.add(new ReportHistory());

        when(reportHistoryRepository.findAll()).thenReturn(reportHistories);

        ResponseEntity<Object> response = reportService.historyReport();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
}
