package com.example.game.payloads.entities.scopa;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ScopaHand(
        List<ScopaCard> enemysCards,
        List<ScopaCard> yourCards,
        List<ScopaCard> tableCards,
        List<ScopaCard> enemysCardsTaken,
        List<ScopaCard> yourCardsTaken,
        @NotNull(message = "Bisogna passare le scope")
        int enemysScopas,
        @NotNull(message = "Bisogna passare le scope")
        int yourScopas,
        @NotNull(message = "Bisogna passare i punti")
        int enemysPoints,
        @NotNull(message = "Bisogna passare i punti")
        int yourPoints,
        @NotNull(message = "Bisogna passare true o false come parametro per isItStart")
        Boolean isItStart,
        @NotEmpty(message = "Di chi è il turno?")
        String tourn,
        Long partitaId
) {
}
