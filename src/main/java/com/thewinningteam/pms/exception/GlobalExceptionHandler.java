package com.thewinningteam.pms.exception;


import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidTokenException(InvalidTokenException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleTokenExpiredException(TokenExpiredException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ErrorResponse handleExpiredJwtException(ExpiredJwtException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @Data
    @AllArgsConstructor
    public static class ErrorResponse {
        private String message;
    }

    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String message) {
            super(message);
        }
    }

    public static class TokenExpiredException extends RuntimeException {
        public TokenExpiredException(String message) {
            super(message);
        }
    }
    @ExceptionHandler(value = {ServiceProviderStatusException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleServiceProviderStatusException(ServiceProviderStatusException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    public static class ServiceProviderStatusException extends RuntimeException {
        public ServiceProviderStatusException(String message) {
            super(message);
        }
    }
}
