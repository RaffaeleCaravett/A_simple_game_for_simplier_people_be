package com.example.game.payloads.entities;

import com.example.game.notification.Notification;
import com.example.game.payloads.entities.scopaHand.ScopaHand;
import lombok.Builder;

@Builder
public record SocketDTO(
        MessageDTO messageDTO,
        MoveDTO moveDTO,
        GameConnectionDTO gameConnectionDTO,
        ConnectionDTO connectionDTO,
        ConnectionRequestDTO connectionRequestDTO,
        Notification notification,
        InvitoDTO invitoDTO,
        ScopaHand scopaHand
) {
}
