package com.example.game.payloads.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GiocoIdAndNameDTO {
    private Long id;
    private String name;
}
