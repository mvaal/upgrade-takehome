package com.marcusvaal.volcanocampsite.advice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();

    @JsonIgnore
    private final HttpStatus httpStatus;

    /**
     * Error message
     */
    private final String message;

    /**
     * Servlet path that the exception took place
     */
    private final String path;

    public int getStatus() {
        return httpStatus.value();
    }

    public String getError() {
        return httpStatus.getReasonPhrase();
    }
}
