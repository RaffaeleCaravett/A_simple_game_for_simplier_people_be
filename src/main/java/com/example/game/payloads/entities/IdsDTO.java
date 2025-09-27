package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record IdsDTO (
        @NotNull(message = "passa almeno un id")
        List<Long> ids
){
}
