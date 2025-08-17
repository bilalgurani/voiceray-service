package com.voiceray.Voiceray.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFileException(
            InvalidFileException ex, WebRequest request) {
        ErrorDetail detail = new ErrorDetail(
                ex.getFieldName(),
                ex.getErrorCode(),
                ex.getMessage()
        );

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_FAILED",
                "File validation error",
                List.of(detail)
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public static class ErrorResponse {
        private int status;
        private String error;
        private String message;
        private List<ErrorDetail> errors;

        public <E> ErrorResponse(int value, String validationFailed, String fileValidationError, List<E> detail) {
        }
    }
    

    public static class ErrorDetail {
        private String field;
        private String code;
        private String message;

        public ErrorDetail(String fieldName, String errorCode, String message) {
        }
    }
}
