package com.ogunkuade.employeemanagementsystem.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedRequestException extends RuntimeException{

    private static final Long serialVersionUID = 1L;

    public UnauthorizedRequestException(String message){
        super(message);
    }


}
