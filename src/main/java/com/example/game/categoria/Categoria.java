package com.example.game.categoria;

import com.example.game.gioco.Gioco;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "categorie_giochi",
    joinColumns = @JoinColumn(name = "categoria_id"),
    inverseJoinColumns = @JoinColumn(name = "gioco_id"))
    @JsonIgnore
    private List<Gioco> giochi;
}
