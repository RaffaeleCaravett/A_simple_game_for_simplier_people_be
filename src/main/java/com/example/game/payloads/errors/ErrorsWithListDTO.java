package com.example.game.payloads.errors;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ErrorsWithListDTO {
    String error;
    Date date;
    List<String> errorList;
}
