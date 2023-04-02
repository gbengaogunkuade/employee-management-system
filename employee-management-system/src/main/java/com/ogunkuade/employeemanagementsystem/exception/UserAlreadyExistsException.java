package com.ogunkuade.employeemanagementsystem.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserAlreadyExistsException extends RuntimeException{

    private static final Long serialVersionUID = 1L;

    public UserAlreadyExistsException(String message){
        super(message);
    }


}
