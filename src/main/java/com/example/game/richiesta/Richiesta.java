package com.example.game.richiesta;

import com.example.game.entityInfos.EntityInfos;
import com.example.game.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "richieste")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Richiesta extends EntityInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String oggetto;
    @Column(length = 2000)
    private String descrizione;
    @Column(length = 100)
    private String descrizionePreview;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
