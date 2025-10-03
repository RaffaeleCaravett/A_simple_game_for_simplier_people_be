package com.example.game.tournament;

import com.example.game.entityInfos.EntityInfos;
import com.example.game.enums.TournamentState;
import com.example.game.gioco.Gioco;
import com.example.game.partita.Partita;
import com.example.game.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "torneo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Tournament extends EntityInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tornei_users",
            joinColumns= @JoinColumn(name = "tornei_id"),
            inverseJoinColumns = @JoinColumn(name = "utenti_id"))
    private List<User> users;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gioco_id")
    private Gioco gioco;
    private LocalDate startDate;
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private TournamentState tournamentState;
    @OneToMany(mappedBy = "tournament",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Partita> partite;
}
