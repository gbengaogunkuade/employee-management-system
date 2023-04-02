package com.ogunkuade.employeemanagementsystem.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UnmatchedPasswordException extends RuntimeException {


    private static final Long serialVersionUID = 1L;

    public UnmatchedPasswordException(String message) {
        super(message);
    }


}
