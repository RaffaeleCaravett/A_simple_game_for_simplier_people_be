package com.example.game.payloads.entities;

import com.example.game.user.User;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserWithPoints {
    private User user;
    private Integer totalPoints;
}
