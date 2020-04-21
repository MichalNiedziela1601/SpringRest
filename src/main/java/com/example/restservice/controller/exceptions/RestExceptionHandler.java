package com.example.restservice.controller.exceptions;

import com.example.restservice.controller.dto.ErrorDto;
import com.example.restservice.controller.dto.ValidationError;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Inject
    private MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rnfe, HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setTimeStamp(new Date().getTime());
        errorDto.setStatus(HttpStatus.NOT_FOUND.value());
        errorDto.setTitle("Resource not found");
        errorDto.setDetail(rnfe.getMessage());
        errorDto.setDeveloperMessage(rnfe.getClass().getName());

        return new ResponseEntity<>(errorDto, null, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setTimeStamp(new Date().getTime());
        errorDto.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDto.setTitle("Validation Failed");
        errorDto.setDetail("Input Validation failed");
        errorDto.setDeveloperMessage(ex.getClass().getName());
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for(FieldError fe : fieldErrors) {
            List<ValidationError> validationErrors = errorDto.getErrors().get(fe.getField());
            if(validationErrors == null) {
                validationErrors = new ArrayList<>();
            }
            errorDto.getErrors().put(fe.getField(), validationErrors);
            ValidationError validationError = new ValidationError();
            validationError.setCode(fe.getCode());
            validationError.setMessage(messageSource.getMessage(fe, null));
            validationErrors.add(validationError);
        }

        return handleExceptionInternal(ex, errorDto, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setTimeStamp(new Date().getTime());
        errorDto.setStatus(status.value());
        errorDto.setTitle("Message not readable");
        errorDto.setDetail(ex.getMessage());
        errorDto.setDeveloperMessage(ex.getClass().getName());

        return handleExceptionInternal(ex, errorDto,headers, status, request);
    }

}
