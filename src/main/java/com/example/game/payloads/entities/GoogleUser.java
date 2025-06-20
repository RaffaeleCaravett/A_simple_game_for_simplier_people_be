package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record GoogleUser(
        @NotBlank(message = "Email mancante")
        String email,
        @NotBlank(message = "Fullname mancante")
        String fullname,
        @NotBlank(message = "Immagine profilo mancante")
        String immagineProfilo
) {
}
