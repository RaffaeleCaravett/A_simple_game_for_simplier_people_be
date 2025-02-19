package com.example.game.classifica;

import com.example.game.gioco.Gioco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "classifiche")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Classifica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(fetch = FetchType.LAZY)
    private Gioco gioco;
}
