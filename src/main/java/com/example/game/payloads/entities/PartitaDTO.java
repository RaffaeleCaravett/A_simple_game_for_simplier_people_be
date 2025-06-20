package com.example.game.payloads.entities;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.springframework.boot.context.properties.bind.DefaultValue;

public record PartitaDTO(
        @NotNull(message = "user necessario")
        long userId,
        @NotNull(message = "gioco necessario")
        long giocoId,
        @NotEmpty(message = "esito necessario")
        String esito,
        @DefaultValue(value = "0")
        @Nullable
        String punteggio
) {
}
