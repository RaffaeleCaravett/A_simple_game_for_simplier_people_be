package com.example.game.preferito;

import com.example.game.extensions.EntityDatas;
import com.example.game.gioco.Gioco;
import com.example.game.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "preferiti")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Preferito extends EntityDatas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne()
    @JoinColumn(name = "gioco_id")
    private Gioco gioco;
    @ManyToOne()
    @JoinColumn(name = "gioco_id")
    private User user;
}
