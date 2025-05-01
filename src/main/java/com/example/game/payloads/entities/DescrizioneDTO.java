package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record DescrizioneDTO(
        @Length(max = 5000)
        @NotBlank(message = "Descrizione mancante")
        String descrizione
) {
}
