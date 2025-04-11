package com.example.game.classifica;

import com.example.game.entityInfos.EntityInfos;
import com.example.game.gioco.Gioco;
import com.example.game.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classifiche")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Classifica extends EntityInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gioco_id")
    private Gioco gioco;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "classifiche_utenti",
            joinColumns = @JoinColumn(name = "classifiche_id"),
            inverseJoinColumns = @JoinColumn(name = "utenti_id"))
    @JsonIgnore
    private List<User> users;


    public void addUser(User user) {
        if(users==null) users = new ArrayList<>();
        if (!users.contains(user)) users.add(user);
    }
}
