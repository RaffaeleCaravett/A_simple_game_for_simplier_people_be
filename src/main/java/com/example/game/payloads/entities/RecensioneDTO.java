package com.example.game.payloads.entities;

import com.example.game.gioco.GiocoService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@Validated
public record RecensioneDTO(
        @NotNull(message = "Inserisci il punteggio.")
        @Min(1)
        @Max(5)
        Integer punteggio,
        @NotNull(message = "Seleziona il gioco.")
        Long giocoId,
        String commento
) {
}
