package com.example.restservice.controller.exceptions;

import com.example.restservice.controller.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class RestExceptionHandler {

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
}
