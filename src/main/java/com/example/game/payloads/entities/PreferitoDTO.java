package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotEmpty;

public record PreferitoDTO(
        @NotEmpty(message = "user_id mancante")
        long user_id,
        @NotEmpty(message = "gioco_id mancante")
        long gioco_id
) {
}
