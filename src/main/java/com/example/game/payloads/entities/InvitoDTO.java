package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record InvitoDTO(
        @NotNull(message = "Gioco id necessario")
        Long giocoId,
        @NotEmpty(message = "stato necessario")
        String status,
        @NotNull(message = "Accepter id necessario")
        Long accepterId,
        Long torneo,
        @NotNull(message = "Sender id necessario")
        Long senderId,
        @NotNull(message = "Invito id necessario")
        Long invitoId
) {
}
