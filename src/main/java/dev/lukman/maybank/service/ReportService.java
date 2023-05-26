package dev.lukman.maybank.service;

import dev.lukman.maybank.api.request.RequestParameterSearch;
import org.springframework.http.ResponseEntity;

public interface ReportService {

    ResponseEntity<Object> searchUser(RequestParameterSearch parameter);

    ResponseEntity<Object> downloadReport(String id);

    ResponseEntity<Object> historyReport();
}
