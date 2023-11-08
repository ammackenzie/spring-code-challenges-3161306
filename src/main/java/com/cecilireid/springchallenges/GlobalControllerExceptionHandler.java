package com.cecilireid.springchallenges;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(HttpClientErrorException.class)
    ResponseEntity<String> handleClientException(HttpClientErrorException exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(exception.getMessage());
    }
}
