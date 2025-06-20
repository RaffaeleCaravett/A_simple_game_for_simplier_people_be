package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserToPatch(
        @NotBlank(message = "Nome vuoto")
        String nome,
        @NotBlank(message = "Cognome vuoto")
        String cognome,
        @Length(max = 5000, message = "Descrizione massimo di 5000 caratteri.")
        String descrizione,
        @NotNull(message = "Città vuota")
        Long cittaId
) {
}
