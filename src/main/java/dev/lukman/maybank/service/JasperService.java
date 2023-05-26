package dev.lukman.maybank.service;

import dev.lukman.maybank.api.request.RequestParameterSearch;
import dev.lukman.maybank.dto.GithubResponse;

public interface JasperService {
    byte[] generateReportPDF(GithubResponse githubResponse, RequestParameterSearch parameter);

    byte[] downloadReport(String fileId);
}
