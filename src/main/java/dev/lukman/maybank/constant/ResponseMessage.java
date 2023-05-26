package dev.lukman.maybank.constant;

public class ResponseMessage {
    public static final String RESPONSE_SEARCH_PARAMETER = "Parameter search is required";
    public static final String RESPONSE_SUCCESS_FETCH = "Successfully get data";
    public static final String RESPONSE_NO_RECORD = "Sorry no record found";
    public static final String MAX_PAGE_MESSAGE = "Page size maximum 100";
    public static final String RESPONSE_INVALID_PAGE = "Invalid size parameter";
    public static final String RESPONSE_FAILED_GENERATE = "Failed to generate report";
    public static final String RESPONSE_FAILED_DOWNLOAD_REPORT = "Failed to download report";
    public static final String RESPONSE_INVALID_TOKEN = "Token is not provided";
    public static final String RESPONSE_SPACE_INVALID_KEY = "Invalid Access Key Id";
    public static final String RESPONSE_SPACE_INVALID_SECRET = "Invalid Access Key Id";
    private ResponseMessage() {
        throw new UnsupportedOperationException("Cannot instantiate a utility class.");
    }
}
