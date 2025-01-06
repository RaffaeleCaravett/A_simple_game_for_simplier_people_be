package com.example.game.exceptions;

import org.springframework.validation.ObjectError;

import java.util.List;

public class BadRequestException extends RuntimeException {

    private List<ObjectError> messages;

    public BadRequestException(String message){
        super(message);
    }
    public BadRequestException(List<ObjectError> messages){
        this.messages=messages;
    }
}
