package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.io.File;
import java.util.List;

public record MessageDTO(
        @NotEmpty(message = "Inserisci il messaggio")
        @Length(max = 5000, message = "Lunghezza massima del messaggio : 5000 caratteri")
        String message,
        @NotNull(message = "Almeno un utente a cui inviare")
        List<Long> riceventi,
        @NotNull(message = "Manca il mittente")
        Long mittente,
        @NotNull(message = "Chat mancante")
        Long chat,
        List<File> messageImages
) {
}
