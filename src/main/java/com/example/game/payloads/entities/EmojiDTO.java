package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record EmojiDTO(
        @NotEmpty(message = "title blank")
        String title,
        @NotEmpty(message = "emoji blank")
        String emoji,
        @NotEmpty(message = "field blank")
        String field

) {
}
