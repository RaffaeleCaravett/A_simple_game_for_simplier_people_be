package com.example.game.payloads.entities;

import com.example.game.enums.MoveType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record MoveDTO(
        @NotNull(message = "id gioco necessario")
        Long id,
        @NotEmpty(message = "id del div necessario")
        String idDiv,
        @NotEmpty(message = "tipo di mossa necessario")
        MoveType moveType,
        Long oppositeUser,
        Long invitationId,
        Long partitaId
) {
}
