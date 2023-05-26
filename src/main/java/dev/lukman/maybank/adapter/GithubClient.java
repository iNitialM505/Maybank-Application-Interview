package dev.lukman.maybank.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lukman.maybank.api.request.RequestParameterSearch;
import dev.lukman.maybank.dto.GithubResponse;
import dev.lukman.maybank.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static dev.lukman.maybank.constant.GlobalConstant.*;
import static dev.lukman.maybank.constant.ResponseMessage.*;

@Component
@RequiredArgsConstructor
public class GithubClient {

    private final RestTemplate restTemplate;

    public GithubResponse searchUsers(RequestParameterSearch parameter) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(DEFAULT_URI)
                .queryParam("q", parameter.getSearch())
                .queryParam("page", parameter.getPage() == 0 ? DEFAULT_PAGE : parameter.getPage())
                .queryParam("order", parameter.getOrder() == null ? DEFAULT_ORDER : parameter.getOrder())
                .queryParam("sort", parameter.getSort() == null ? DEFAULT_SORT : parameter.getSort())
                .queryParam("per_page", parameter.getSize() == 0 ? DEFAULT_SIZE_PAGE : parameter.getSize());
        try {
            ResponseEntity<GithubResponse> responseEntity = restTemplate.getForEntity(uriBuilder.toUriString(), GithubResponse.class);
            GithubResponse response = responseEntity.getBody();
            if (response != null && response.getTotalCount() <= 0)
                throw new ErrorException(HttpStatus.NOT_FOUND.value(), RESPONSE_NO_RECORD, HttpStatus.NOT_FOUND);
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            ObjectMapper mapper = new ObjectMapper();
            String errorMessage = "An error occurred";
            try {
                JsonNode root = mapper.readTree(e.getResponseBodyAsString());
                errorMessage = root.path("message").asText();
            } catch (IOException ignored) { }
            throw new ErrorException(e.getRawStatusCode(), errorMessage, e.getStatusCode());
        }
    }
}
