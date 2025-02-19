package com.example.game.trofeo;

import com.example.game.gioco.Gioco;
import jakarta.persistence.*;

@Entity
@Table(name = "trofeo")
public class Trofeo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gioco_id")
    private Gioco gioco;

}
