package dev.lukman.maybank.api.request;

import lombok.Data;

@Data
public class RequestParameterSearch {
    private String search;
    private String sort;
    private String order;
    private int page;
    private int size;
}
