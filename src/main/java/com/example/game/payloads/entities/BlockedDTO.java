package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotNull;

public record BlockedDTO(
        @NotNull(message = "L'utente id è necessario")
        Long utente_id
) {
}
