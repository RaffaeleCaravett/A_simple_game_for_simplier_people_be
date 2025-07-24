package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotNull;
import lombok.*;


public record ReadReceiptRequestDTO(
        @NotNull(message = "Chat id necessario")
        Long chatId,
        @NotNull(message = "Sender id necessario")
        Long senderId
) {
}
