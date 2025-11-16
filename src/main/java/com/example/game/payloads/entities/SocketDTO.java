package com.example.game.payloads.entities;

import com.example.game.notification.Notification;
import com.example.game.payloads.entities.scopa.ScopaDone;
import com.example.game.payloads.entities.scopa.ScopaHand;
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
        ScopaHand scopaHand,
        GameEnd gameEnd,
        ScopaDone scopaDone
) {
}
