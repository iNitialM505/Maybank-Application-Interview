package dev.lukman.maybank.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorException extends RuntimeException {
    private int errorCode;
    private String errorMessage;
    private HttpStatus httpStatus;
}
