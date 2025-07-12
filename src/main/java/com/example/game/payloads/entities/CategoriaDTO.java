package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotEmpty;

public record CategoriaDTO(
        @NotEmpty(message = "Categoria necessaria")
        String nome
) {
}
