package dev.lukman.maybank.adapter;

import dev.lukman.maybank.api.request.RequestParameterSearch;
import dev.lukman.maybank.dto.GithubResponse;
import dev.lukman.maybank.exception.ErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GithubClient githubClient;

    private RequestParameterSearch requestParameterSearch;

    @BeforeEach
    public void setUp() {
        requestParameterSearch = new RequestParameterSearch();
        requestParameterSearch.setSearch("openai");
        requestParameterSearch.setSort("joined");
        requestParameterSearch.setOrder("desc");
        requestParameterSearch.setPage(1);
        requestParameterSearch.setSize(10);
    }

    @Test
    void testSearchUsers_success() {
        GithubResponse response = new GithubResponse();
        response.setTotalCount(1);
        when(restTemplate.getForEntity(any(String.class), eq(GithubResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        GithubResponse result = githubClient.searchUsers(requestParameterSearch);

        assertEquals(response, result);
        verify(restTemplate, times(1)).getForEntity(any(String.class), eq(GithubResponse.class));
    }

    @Test
    void testSearchUsers_notFound() {
        GithubResponse response = new GithubResponse();
        response.setTotalCount(0);
        when(restTemplate.getForEntity(any(String.class), eq(GithubResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        assertThrows(ErrorException.class, () -> githubClient.searchUsers(requestParameterSearch));
        verify(restTemplate, times(1)).getForEntity(any(String.class), eq(GithubResponse.class));
    }

    @Test
    void testSearchUsers_exception() {
        when(restTemplate.getForEntity(any(String.class), eq(GithubResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

        assertThrows(ErrorException.class, () -> githubClient.searchUsers(requestParameterSearch));
        verify(restTemplate, times(1)).getForEntity(any(String.class), eq(GithubResponse.class));
    }
}

