package com.example.game.exceptions;

import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
public class BadRequestException extends RuntimeException {

    private String error;
    private List<ObjectError> messageList;

    public BadRequestException(String message){
        super(message);
    }
    public BadRequestException(List<ObjectError> messages){
        this.messageList=messages;
    }
}
