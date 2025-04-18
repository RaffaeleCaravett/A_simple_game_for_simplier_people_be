package com.example.game.payloads.entities;

import com.example.game.gioco.Gioco;
import com.example.game.user.User;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassificaWithStatisticsDTO {
    private Long id;
    private Gioco gioco;
    private HashMap<Integer,UserWithPoints> users;
}
