package dev.lukman.maybank.configuration;

import dev.lukman.maybank.exception.ErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import static dev.lukman.maybank.constant.GlobalConstant.*;
import static dev.lukman.maybank.constant.ResponseMessage.*;

@Configuration
public class HttpClientConfig {

    @Value("${api.github.token}")
    private String authorizationHeader;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getInterceptors().add(loggingInterceptor());
        return restTemplate;
    }

    private ClientHttpRequestInterceptor loggingInterceptor() {
        return (request, body, execution) -> {
            checkParameter(UriComponentsBuilder.fromUriString(request.getURI().toString()).build());
            setupHeaders(request.getHeaders());
            return execution.execute(request, body);
        };
    }

    private void checkParameter(UriComponents components) {
        String searchParameter = components.getQueryParams().getFirst("q");
        if (searchParameter == null) {
            throw new ErrorException(HttpStatus.BAD_REQUEST.value(), RESPONSE_SEARCH_PARAMETER, HttpStatus.BAD_REQUEST);
        }

        String sizeParameter = components.getQueryParams().getFirst("per_page");
        if (sizeParameter != null) {
            try {
                int perPage = Integer.parseInt(sizeParameter);
                if (perPage > 100) {
                    throw new ErrorException(HttpStatus.BAD_REQUEST.value(), MAX_PAGE_MESSAGE, HttpStatus.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                throw new ErrorException(HttpStatus.BAD_REQUEST.value(), RESPONSE_INVALID_PAGE, HttpStatus.BAD_REQUEST);
            }

        }
    }

    private void setupHeaders(HttpHeaders headers) {
        if(authorizationHeader == null || authorizationHeader.isEmpty())
            throw new ErrorException(HttpStatus.BAD_REQUEST.value(), RESPONSE_INVALID_TOKEN, HttpStatus.BAD_REQUEST);

        headers.setBearerAuth(authorizationHeader);
        headers.set("Accept", CONTENT_TYPE);
        headers.set("X-GitHub-Api-Version", GITHUB_API_VERSION);
    }

    @Bean
    public URI defaultEndpoint() throws URISyntaxException {
        return new URI(DEFAULT_URI);
    }
}
