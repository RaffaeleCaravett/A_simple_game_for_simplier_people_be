package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.chrono.IsoChronology;

public record TournamentDTO(
        @NotEmpty(message = "Nome torneo necessario")
        String nome,
        @NotNull(message = "Gioco id necessario")
        Long gioco_id,
        @NotNull(message = "Data inizio necessaria")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dateFrom,
        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dateTo
        ) {
}
