package com.example.game.user;

import com.example.game.citta.Citta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String immagineProfilo;
    @ManyToOne(fetch = FetchType.EAGER)
    @Column(name = "citta_id")
    private Citta citta;
}
