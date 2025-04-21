package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserToPatch(
        @NotBlank(message = "Nome vuoto")
        String nome,
        @NotBlank(message = "Cognome vuoto")
        String cognome,
        @NotNull(message = "Città vuota")
        Long cittaId
) {
}
