package com.example.game.partita;

import com.example.game.entityInfos.EntityInfos;
import com.example.game.enums.Esito;
import com.example.game.enums.GameType;
import com.example.game.gioco.Gioco;
import com.example.game.punteggio.Punteggio;
import com.example.game.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "partite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Partita extends EntityInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private Esito esito;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gioco_id")
    private Gioco gioco;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "partita")
    @JoinColumn(name = "punteggio_id")
    private Punteggio punteggio;
    @Enumerated(EnumType.STRING)
    private GameType gameType;
}
