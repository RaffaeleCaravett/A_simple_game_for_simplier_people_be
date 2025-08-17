package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotNull;

public record ConnectionRequestDTO (
    @NotNull(message = "Receiver id necessario")
    Long receiverId
){
}
