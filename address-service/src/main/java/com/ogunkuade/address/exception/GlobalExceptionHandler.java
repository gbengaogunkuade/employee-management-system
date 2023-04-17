package com.ogunkuade.address.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ControllerAdvice
public class GlobalExceptionHandler {



    //EXCEPTION HANDLER FOR VALIDATION - MethodArgumentNotValidException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ValidationResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        List<String> errorList = new ArrayList<>();
        for(ObjectError error : methodArgumentNotValidException.getBindingResult().getAllErrors()) {
            errorList.add(error.getDefaultMessage());
        }
        ValidationResponse validationResponse = new ValidationResponse(new Date(),"VALIDATION EXCEPTION", errorList);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResponse);
    }


    //EXCEPTION HANDLER FOR AddressNotFoundException
    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAddressNotFoundException(AddressNotFoundException addressNotFoundException){
        ErrorResponse errorResponse = new ErrorResponse(new Date(), "ADDRESS NOT FOUND", addressNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }



    //EXCEPTION HANDLER FOR OTHER Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception){
        ErrorResponse errorResponse = new ErrorResponse(new Date(), "EXCEPTION", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


}
