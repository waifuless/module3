package com.epam.esm.gcs.web.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {

    private String errorMessage;
    private String errorCode;

    public ErrorResponse(String errorMessage, HttpStatus errorCode, Class<?> targetClass) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode.value() + ErrorCodePostfix.findPostfixByClass(targetClass);
    }

    public ErrorResponse(String errorMessage, HttpStatus errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = String.valueOf(errorCode.value());
    }
}
