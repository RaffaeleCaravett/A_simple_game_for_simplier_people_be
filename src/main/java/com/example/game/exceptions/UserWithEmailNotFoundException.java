package com.example.game.exceptions;

public class UserWithEmailNotFoundException extends RuntimeException{
    public UserWithEmailNotFoundException(String message){
        super("User non trovato con email : " + message);
    }
}
