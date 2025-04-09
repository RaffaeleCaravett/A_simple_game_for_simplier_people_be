package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotNull;

public record ClassificaDTO(
        @NotNull(message = "Gioco Id necessario")
        Long giocoId
)
{
}
