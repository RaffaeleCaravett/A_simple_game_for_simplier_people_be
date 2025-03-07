package com.example.game.recensione;

import com.example.game.entityInfos.EntityInfos;
import com.example.game.gioco.Gioco;
import com.example.game.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recensioni")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Recensione extends EntityInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String commento;
    private int punteggio;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gioco_id")
    @JsonIgnore
    private Gioco gioco;

}
