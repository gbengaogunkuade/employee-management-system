package com.ogunkuade.address.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class AddressNotFoundException extends RuntimeException {


    private static final Long serialVersionUID = 1L;


    public AddressNotFoundException(String message){
        super(message);
    }


}
