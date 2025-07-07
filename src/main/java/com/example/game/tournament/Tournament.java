package com.example.game.tournament;

import com.example.game.gioco.Gioco;
import com.example.game.punteggio.Punteggio;
import com.example.game.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "torneo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tournament {
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
    @OneToMany(mappedBy = "torneo",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Punteggio> punteggios;
    private LocalDate startDate;
    private LocalDate endDate;
}
