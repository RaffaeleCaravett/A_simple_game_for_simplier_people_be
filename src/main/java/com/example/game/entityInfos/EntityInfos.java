package com.example.game.entityInfos;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class EntityInfos {
    private boolean isActive;
    private String createdAt;
    private LocalDate createdAtDate;
    @Nullable
    private String modifiedAt;
    @Nullable
    private String deletedAt;
}
