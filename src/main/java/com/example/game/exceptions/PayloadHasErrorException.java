package com.example.game.exceptions;

public class PayloadHasErrorException extends RuntimeException {
  public PayloadHasErrorException(String message) {
    super(message);
  }
}
