package com.voiceray.Voiceray.exception;

public class InvalidFileException extends RuntimeException {
    private final String fieldName;
    private final String errorCode;

    /**
     * Constructs a new file validation exception
     *
     * @param message Detailed error message
     * @param fieldName Name of the form field that caused the error
     * @param errorCode Machine-readable error code
     */
    public InvalidFileException(String message, String fieldName, String errorCode) {
        super(message);
        this.fieldName = fieldName;
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new file validation exception with cause
     *
     * @param message Detailed error message
     * @param fieldName Name of the form field that caused the error
     * @param errorCode Machine-readable error code
     * @param cause Root cause exception
     */
    public InvalidFileException(String message, String fieldName, String errorCode, Throwable cause) {
        super(message, cause);
        this.fieldName = fieldName;
        this.errorCode = errorCode;
    }

    /**
     * @return Name of the form field that caused the error
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @return Machine-readable error code for client handling
     */
    public String getErrorCode() {
        return errorCode;
    }
}
