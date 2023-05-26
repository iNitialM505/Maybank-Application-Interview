package dev.lukman.maybank.api.response;

import dev.lukman.maybank.dto.ReportHistoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ResponseSuccess {
    private String message;
    private List<ReportHistoryDTO> data;
}
