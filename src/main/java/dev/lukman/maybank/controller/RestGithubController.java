package dev.lukman.maybank.controller;

import dev.lukman.maybank.api.request.RequestParameterSearch;
import dev.lukman.maybank.service.impl.ReportServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/report")
@RequiredArgsConstructor
public class RestGithubController {

    private final ReportServiceImpl reportService;

    @GetMapping
    public ResponseEntity<Object> searchReport(RequestParameterSearch parameter){
        return reportService.searchUser(parameter);
    }

    @GetMapping("/download")
    public ResponseEntity<Object> historyReport(@RequestParam String id){
        return reportService.downloadReport(id);
    }


    @GetMapping("/history")
    public ResponseEntity<Object> historyReport(){
        return reportService.historyReport();
    }
}
