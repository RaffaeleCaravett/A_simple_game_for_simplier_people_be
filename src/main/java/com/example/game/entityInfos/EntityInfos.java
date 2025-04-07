package com.example.game.entityInfos;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder(toBuilder=true)
public abstract class EntityInfos {
    private boolean isActive;
    private String createdAt;
    private LocalDate createdAtDate;
    @Nullable
    private String modifiedAt;
    @Nullable
    private String deletedAt;
}
