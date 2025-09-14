package com.example.game.blocked;

import com.example.game.entityInfos.EntityInfos;
import com.example.game.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "blocked")
@Getter
@Setter
@Data
@SuperBuilder
public class Blocked extends EntityInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
