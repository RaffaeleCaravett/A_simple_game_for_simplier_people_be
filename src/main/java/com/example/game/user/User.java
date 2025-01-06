package com.example.game.user;

import com.example.game.citta.Citta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
    private boolean isActive;
    private String createdAt;
    private LocalDate createdAtDate;
    private String deletedAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @Column(name = "citta_id")
    private Citta citta;
}
