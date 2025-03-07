package com.example.game.exceptions;

public class ValidationAlreadyExistsException extends RuntimeException{
    public ValidationAlreadyExistsException(String message){
        super(message);
    }
}
