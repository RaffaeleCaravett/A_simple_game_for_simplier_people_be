package com.example.game.exceptions;

public class UserWithIdNotFoundException extends RuntimeException
{
    public UserWithIdNotFoundException(long id){
        super("User con id " + id + " non trovato. Ricordati di riattivare il tuo account se ne avevi uno.");
    }
}
