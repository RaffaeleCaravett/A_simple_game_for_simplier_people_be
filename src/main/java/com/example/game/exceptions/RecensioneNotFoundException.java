package com.example.game.exceptions;

public class RecensioneNotFoundException extends RuntimeException{
    public RecensioneNotFoundException(long id){
        super("Recensione non trovata con id : " + id);
    }
}
