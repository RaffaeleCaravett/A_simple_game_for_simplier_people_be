package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record DescrizioneDTO(
        @Length(max = 5000)
        String innerHTML,
        String textAlignmet
) {
}
