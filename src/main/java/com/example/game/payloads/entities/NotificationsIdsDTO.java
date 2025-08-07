package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record NotificationsIdsDTO (
        @NotNull(message = "passa almeno un id")
        List<Long> ids
){
}
