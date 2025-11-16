package com.example.game.payloads.entities.scopa;

import jakarta.validation.constraints.NotNull;

public record ScopaDone(
        @NotNull(message = "User id necessario")
        Long userId,
        @NotNull(message = "Partita id necessario")
        Long partitaDoubleId
) {
}
