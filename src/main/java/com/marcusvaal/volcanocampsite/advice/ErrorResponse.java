package com.marcusvaal.volcanocampsite.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();

    private final String message;
}
