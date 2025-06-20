package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotEmpty;

public record CittaDTO (
        @NotEmpty(message = "Nome necessario.")
        String nome
) {
}
