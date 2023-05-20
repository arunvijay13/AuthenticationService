package com.service.authservice.exception;

import com.service.authservice.model.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorResponse errorResponse = new ErrorResponse();

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<LinkedHashMap<String, Object>> errorList = new ArrayList<>();
        ex.getFieldErrors().forEach((error) -> {
            LinkedHashMap<String, Object> errorMap = new LinkedHashMap<>();
            errorMap.put("fieldName" , error.getField());
            errorMap.put("fieldValue",  error.getRejectedValue());
            errorMap.put("message", error.getDefaultMessage());
            errorList.add(errorMap);
        });
        return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> userExceptionHandler(UsernameNotFoundException cause) {
        errorResponse.setMessage(cause.getClass().getSimpleName());
        errorResponse.setDescription(cause.getMessage());
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> userExceptionHandler(UserAlreadyExistException cause) {
        errorResponse.setMessage(cause.getClass().getSimpleName());
        errorResponse.setDescription(cause.getMessage());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> userExceptionHandler(BadCredentialsException cause) {
        errorResponse.setMessage(cause.getClass().getSimpleName());
        errorResponse.setDescription(cause.getMessage());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
