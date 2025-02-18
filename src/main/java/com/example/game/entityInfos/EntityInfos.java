package com.example.game.entityInfos;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class EntityInfos {
    private boolean isActive;
    private String createdAt;
    private LocalDate createdAtDate;
    private String deletedAt;
}
