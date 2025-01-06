package com.example.game.exceptions;

public class CityNotFoundException extends RuntimeException{

    public CityNotFoundException(long id){
        super("Città con id " + id + " non trovata in db.");
    }
}
