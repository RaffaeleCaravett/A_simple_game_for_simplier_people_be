package com.example.game.payloads.entities;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record ChatDTO(
        @NotNull(message = "Passa gli id degli utenti")
        @Size(min = 2, message = "Non puoi avviare una chat singola.")
        List<Long> userId,
        @Nullable
        @Length(max = 30, message = "Il titolo può aver massimo 30 caratteri.")
        String title
) {
}
