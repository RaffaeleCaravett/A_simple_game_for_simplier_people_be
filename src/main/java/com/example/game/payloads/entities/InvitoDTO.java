package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record InvitoDTO(
        @NotNull(message = "Gioco id necessario")
        Long giocoId,
        @NotNull(message = "Receiver id necessario")
        Long receiverId
) {
}
