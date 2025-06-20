package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserLoginDTO(
        @NotEmpty(message = "Email necessaria")
        @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "L'email inserita non è valida")
        String email,
        @NotEmpty(message = "Password necessaria")
        @Size(min = 8, max=30, message = "La password deve essere compresa tra 8 e 30 caratteri")
        String password
) {
}
