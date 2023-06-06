package com.thss.androidbackend.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity handleException(CustomException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity handleException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
