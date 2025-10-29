package com.example.game.invito;

import com.example.game.enums.InviteState;
import com.example.game.gioco.Gioco;
import com.example.game.partita.Partita;
import com.example.game.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "inviti")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant createdAt;
    @ManyToOne
    @JoinColumn(name = "gioco_id")
    private Gioco gioco;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @Enumerated(EnumType.STRING)
    private InviteState inviteState;
    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Partita partita;
}
