package dev.lukman.maybank.constant;

public class GlobalConstant {
    public static final String DEFAULT_URI = "https://api.github.com/search/users";
    public static final String CONTENT_TYPE = "application/vnd.github+json";
    public static final String GITHUB_API_VERSION = "2022-11-28";
    public static final int DEFAULT_SIZE_PAGE = 5;
    public static final String DEFAULT_SORT = "repositories";
    public static final String DEFAULT_ORDER = "desc";
    public static final int DEFAULT_PAGE = 1;
    public static final String FILE_NAME = "report.pdf";
    public static final String PATH_TEMPLATE_REPORT_GITHUB = "/templates/github.jrxml";
    private GlobalConstant() {
        throw new UnsupportedOperationException("Cannot instantiate a utility class.");
    }
}
