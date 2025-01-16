package com.pp1.salve.api.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import com.pp1.salve.exceptions.NoDuplicatedEntityException;
import com.pp1.salve.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;


@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> errors = new ArrayList<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("fieldName", ((FieldError) error).getField());
            errorDetails.put("defaultMessage", error.getDefaultMessage());
            errors.add(errorDetails);
        });
        
        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> tratarErro404(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> tratarErro500(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("cause", ex.getCause() != null ? ex.getCause().toString() : "N/A");
        //response.put("stackTrace", ex.getStackTrace());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<Map<String, Object>> tratarErro404(HttpClientErrorException.NotFound ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("cause", ex.getCause() != null ? ex.getCause().toString() : "N/A");
        response.put("respostaDoServidor", ex.getResponseBodyAs(Object.class));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAuthorizationDeniedException(org.springframework.security.authorization.AuthorizationDeniedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Access Denied");
        response.put("details", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(HttpClientErrorException.BadRequest ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("cause", ex.getCause() != null ? ex.getCause().toString() : "N/A");
        response.put("respostaDoServidor", ex.getResponseBodyAs(Object.class));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NoDuplicatedEntityException.class)
    public ResponseEntity<Map<String, Object>> handleNoDuplicatedEntityException(NoDuplicatedEntityException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}