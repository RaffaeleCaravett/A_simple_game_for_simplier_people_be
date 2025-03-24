package com.example.game.payloads.entities;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RecensioneGiocoDTO(
        @NotNull(message = "Inserisci l'id.")
        long id,
        @NotNull(message = "Inserisci il punteggio.")
        @Min(1)
        @Max(5)
        Integer punteggio,
        @NotEmpty(message = "Inserisci il nome del gioco.")
        String giocoName,
        @NotEmpty(message = "Inserisci l'immagine del gioco.")
        byte[] giocoImage,
        String commento,
        @NotEmpty(message = "Inserisci la data di pubblicazione.")
        String createdAt,
        @NotEmpty(message = "Inserisci il nome completo.")
        String userFullName,
        @NotEmpty(message = "Inserisci l'immagine del profilo.")
        String userProfileImage
) {
}
