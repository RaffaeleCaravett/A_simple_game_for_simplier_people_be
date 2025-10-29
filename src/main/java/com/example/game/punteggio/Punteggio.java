package com.example.game.punteggio;

import com.example.game.entityInfos.EntityInfos;
import com.example.game.partita.Partita;
import com.example.game.partitaDouble.PartitaDouble;
import com.example.game.tournament.Tournament;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "punteggi")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Punteggio extends EntityInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Partita partita;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partita_double_id")
    @JsonIgnore
    private PartitaDouble partitaDouble;
    private String punteggio;


    public Punteggio(Partita partita, String punteggio) {
        this.partita = partita;
        this.punteggio = punteggio;
    }
}
