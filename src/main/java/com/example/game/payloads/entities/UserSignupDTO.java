package com.example.game.payloads.entities;

import jakarta.validation.constraints.*;


public record UserSignupDTO(
        @NotEmpty(message = "Email necessaria")
        @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "L'email inserita non è valida")
        String email,
        @NotEmpty(message = "Password necessaria")
        @Size(min = 8, max=30, message = "La password deve essere compresa tra 8 e 30 caratteri")
        String password,
        @NotEmpty(message = "Nome necessario")
        @Size(min = 1, max=30, message = "Il nome deve essere compreso tra 1 e 30 caratteri")
        String nome,
        @NotEmpty(message = "Cognome necessario")
        @Size(min = 1, max=30, message = "Il cognome deve essere compreso tra 1 e 30 caratteri")
        String cognome,
        @NotNull(message = "Id della città necessario")
        @Max(value = 1000, message = "Id massimo : 1000")
        Long cittaId
) {
}
