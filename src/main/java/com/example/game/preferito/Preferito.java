package com.example.game.preferito;

import com.example.game.entityInfos.EntityInfos;
import com.example.game.extensions.EntityDatas;
import com.example.game.gioco.Gioco;
import com.example.game.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "preferiti")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Preferito extends EntityInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gioco_id")
    private Gioco gioco;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public void delete(){
        setActive(false);
        setModifiedAt(String.valueOf(LocalDate.now()));
        setDeletedAt(String.valueOf(LocalDate.now()));
    }
}
