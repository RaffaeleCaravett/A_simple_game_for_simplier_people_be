package com.example.game.richiesta;

import com.example.game.entityInfos.EntityInfos;
import com.example.game.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "richieste")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Richiesta extends EntityInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String oggetto;
    @Column(length = 2000)
    private String descrizione;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
