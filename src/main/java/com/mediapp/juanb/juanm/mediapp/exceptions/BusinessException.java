package com.mediapp.juanb.juanm.mediapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Código 400
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}