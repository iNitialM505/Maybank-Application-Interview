package dev.lukman.maybank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubResponse{
    @JsonProperty("total_count")
    private Integer totalCount;
    @JsonProperty("incomplete_results")
    private Boolean incompleteResults;
    @JsonProperty("items")
    private List<GithubUserDTO> items;
}