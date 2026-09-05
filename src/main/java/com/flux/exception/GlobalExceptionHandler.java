package com.flux.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.charset.MalformedInputException;

@RestControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        @ExceptionHandler(ReportAlreadyExistsException.class)
    public ResponseEntity<String> handleReportAlreadyExistsException(ReportAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
        @ExceptionHandler(MalformedInputException.class)
        public ResponseEntity<String> handleMalformedInput(
                MalformedInputException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid CSV file. File must be UTF-8 encoded.");
        }

}
