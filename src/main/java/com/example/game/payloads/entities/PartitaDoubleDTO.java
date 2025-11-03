package com.example.game.payloads.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PartitaDoubleDTO(
        @NotNull(message = "Il gioco è obbligatorio")
        Long gioco,
        @NotNull(message = "L'invito è obbligatorio")
        Long invito,
        @NotNull(message = "Inserisci i partecipanti.")
        @Size(min = 2, message = "Minimo 2 persone.")
        List<Long> partecipanti,
        Long torneo,
        String punteggioVincenti,
        String punteggioPerdenti,
        List<Long> vincitori
) {
}
