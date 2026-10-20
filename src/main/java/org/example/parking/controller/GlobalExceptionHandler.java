package org.example.parking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCity(
            IllegalArgumentException exception) {

        return ResponseEntity.badRequest().body(
                Map.of("error", exception.getMessage())
        );
    }

    @ExceptionHandler({
            RestClientException.class,
            IllegalStateException.class
    })
    public ResponseEntity<Map<String, String>> handleProviderError(
            Exception exception) {

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
                Map.of("error", "Parking provider unavailable or invalid")
        );
    }
}
