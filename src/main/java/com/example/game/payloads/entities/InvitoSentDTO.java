package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotNull;

public record InvitoSentDTO(
        @NotNull(message = "Gioco id necessario")
        Long giocoId
) {
}
