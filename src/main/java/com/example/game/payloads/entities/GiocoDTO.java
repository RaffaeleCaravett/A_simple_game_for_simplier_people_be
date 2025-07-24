package com.example.game.payloads.entities;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public record GiocoDTO (
        String nome,
        @Length(max = 5000, message = "La descrizione può avere massimo 5000 caratteri.")
        String descrizione,
        @Nullable
        @Min(value = 1,message = "Valore minimo della difficoltà : 1.")
        @Max(value = 5,message = "Valore massimo della difficoltà : 5.")
        Integer difficolta,
        List<Long> categorie
){
}
