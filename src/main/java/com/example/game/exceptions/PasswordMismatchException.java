package com.example.game.exceptions;

public class PasswordMismatchException extends RuntimeException{
    public PasswordMismatchException (String message){
        super(message);
    }
}
