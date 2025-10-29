package com.example.game.partitaDouble;

import com.example.game.entityInfos.EntityInfos;
import com.example.game.gioco.Gioco;
import com.example.game.invito.Invito;
import com.example.game.punteggio.Punteggio;
import com.example.game.tournament.Tournament;
import com.example.game.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "partita_double")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PartitaDouble extends EntityInfos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "partite_double_partecipanti",
            joinColumns = @JoinColumn(name = "partita_double_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> partecipanti;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "gioco_id")
    private Gioco gioco;
    private String punteggioPerdenti;
    @OneToOne(mappedBy = "partitaDouble")
    private Punteggio punteggioVincenti;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "partite_double_vincenti",
            joinColumns = @JoinColumn(name = "partita_double_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> vincitori;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "invito_id")
    private Invito invito;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "torneo_id")
    private Tournament tournament;
}
