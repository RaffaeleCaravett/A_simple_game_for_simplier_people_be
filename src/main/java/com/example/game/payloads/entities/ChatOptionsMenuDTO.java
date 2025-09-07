package com.example.game.payloads.entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatOptionsMenuDTO {
    private List<String> options;
}
