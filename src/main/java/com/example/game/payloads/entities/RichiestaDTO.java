package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RichiestaDTO(
        @NotBlank(message = "Inserisci l'oggetto")
        String oggetto,
        @NotBlank(message = "Inserisci la descrizione")
        String descrizione,
        @NotNull(message = "Inserisci l'id dello user")
        Long userId
) {
}
