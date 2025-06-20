package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PreferitoDTO(
        @NotNull(message = "user_id mancante")
        long user_id,
        @NotNull(message = "gioco_id mancante")
        long gioco_id
) {
}
