package com.example.game.payloads.entities;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class StatsDTO {

    private int userCount;
    private int gamesCount;
    private int torneiCount;
    private int categoriesCount;
}
