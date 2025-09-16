package com.example.game.blocked;

import com.example.game.entityInfos.EntityInfos;
import com.example.game.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "blocked")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Blocked extends EntityInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User blocked;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "blocker_id")
    @JsonIgnore
    private User blocker;
}
