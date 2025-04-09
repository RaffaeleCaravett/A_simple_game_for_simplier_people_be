package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotNull;

public record TrofeoDTO (
        @NotNull(message = "Gioco Id necessario")
        Long giocoId,
        @NotNull(message = "User Id necessario")
        Long userId){
}
