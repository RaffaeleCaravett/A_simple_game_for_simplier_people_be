package com.example.game.exceptions;

public class GiocoNotFoundException extends RuntimeException{
    public GiocoNotFoundException(long id){
        super("Gioco con id " + id + " non trovato in database.");
    }
}
