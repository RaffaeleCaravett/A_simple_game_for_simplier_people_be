package com.example.game.emoji;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "emojies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Emoji {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String emoji;
    private String field;
}
