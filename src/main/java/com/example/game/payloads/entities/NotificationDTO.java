package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotNull;

public record NotificationDTO(
        @NotNull(message = "Chat id mancante")
        Long chatId,
        @NotNull(message = "Sender id mancante")
        Long senderId,
        @NotNull(message = "Receiver id mancante")
        Long receiverId,
        String testo
) {
}
