package com.example.game.payloads.entities.scopa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScopaCard {
    private String value;
    private String group;
    private int primeraValue;
}
