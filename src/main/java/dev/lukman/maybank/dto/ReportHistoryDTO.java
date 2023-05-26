package dev.lukman.maybank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.lukman.maybank.model.ReportHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ReportHistoryDTO {
    private Long id;
    @JsonProperty("file_id")
    private String fileId;
    private String parameter;
    @JsonProperty("total_data")
    private int totalData;
    @JsonProperty("created_at")
    private Date createdAt;

    public ReportHistory toModel() {
        return new ReportHistory()
                .setFileId(fileId)
                .setParameter(parameter)
                .setTotalData(totalData);
    }
}
