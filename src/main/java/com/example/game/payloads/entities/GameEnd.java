package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record GameEnd(
        @NotNull(message = "Game Id necessario")
        Long gameId,
        @NotNull(message = "Partita double id necessario")
        Long partitaDoubleId,
        @NotNull(message = "Winner necessario")
        Long winner,
        @NotEmpty(message = "Punteggio necessario")
        String punteggio
) {
}
