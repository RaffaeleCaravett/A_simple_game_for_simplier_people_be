package com.example.game.payloads.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameConnectionDTO {
    private Long giocoId;
    private Long userId;
    private Boolean connected;
}
