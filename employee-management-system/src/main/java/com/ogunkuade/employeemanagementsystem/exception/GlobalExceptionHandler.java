package com.ogunkuade.employeemanagementsystem.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.*;


@ControllerAdvice
public class GlobalExceptionHandler {

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


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException accessDeniedException){
        ErrorResponse errorResponse = new ErrorResponse(new Date(), "ACCESS DENIED EXCEPTION", "Sorry, only a user with Admin role can perform this task");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException userNotFoundException){
        ErrorResponse errorResponse = new ErrorResponse(new Date(), "USER NOT FOUND EXCEPTION", userNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException userAlreadyExistsException){
        ErrorResponse errorResponse = new ErrorResponse(new Date(), "USER ALREADY EXISTS EXCEPTION", userAlreadyExistsException.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }


    @ExceptionHandler(UnmatchedPasswordException.class)
    public ResponseEntity<ErrorResponse> handleUnmatchedPasswordException(UnmatchedPasswordException unmatchedPasswordException){
        ErrorResponse errorResponse = new ErrorResponse(new Date(), "UNMATCHED PASSWORD EXCEPTION", unmatchedPasswordException.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }


    @ExceptionHandler(UnauthorizedRequestException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedRequestException(UnauthorizedRequestException unauthorizedRequestException){
        ErrorResponse errorResponse = new ErrorResponse(new Date(), "UNAUTHORIZED REQUEST", unauthorizedRequestException.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception){
        ErrorResponse errorResponse = new ErrorResponse(new Date(), "EXCEPTION", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAddressNotFoundException(AddressNotFoundException addressNotFoundException){
        ErrorResponse errorResponse = new ErrorResponse(new Date(), "ADDRESS NOT FOUND", addressNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }



}
