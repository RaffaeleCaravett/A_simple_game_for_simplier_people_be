package com.example.game.gioco;

import com.example.game.classifica.Classifica;
import com.example.game.trofeo.Trofeo;
import com.example.game.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "giochi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gioco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nomeGioco;
    private int difficolta;
    private byte[] image;
    @OneToOne(mappedBy = "gioco", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private Classifica classifica;
    @OneToMany(mappedBy = "gioco", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Trofeo> trofeo;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "giochi_utenti",
            joinColumns= @JoinColumn(name = "giochi_id"),
            inverseJoinColumns = @JoinColumn(name = "utenti_id"))
    @JsonIgnore
    private List<User> users;


}
