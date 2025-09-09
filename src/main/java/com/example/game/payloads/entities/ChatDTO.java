package com.example.game.payloads.entities;

import com.example.game.enums.ChatType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record ChatDTO(
        @NotNull(message = "Passa gli id degli utenti")
        @Size(min = 2, message = "Non puoi avviare una chat singola.")
        List<Long> userId,
        @Nullable
        @Length(max = 30, message = "Il titolo può aver massimo 30 caratteri.")
        String title,
        @Nullable
        String chatType
) {
}
