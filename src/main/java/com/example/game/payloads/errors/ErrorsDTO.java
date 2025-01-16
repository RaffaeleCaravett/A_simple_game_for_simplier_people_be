package com.example.game.payloads.errors;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public class ErrorsDTO {
    String message;
    Date date;
}
