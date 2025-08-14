package com.example.game.payloads.entities;

import com.example.game.enums.InviteState;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoveToHandleDTO {
    private Long giocoId;
    private Long moverId;
    private Long partitaId;
    private InviteState inviteState;
    private Long senderId;
    private Long receiverId;
    private Long senderScore;
    private Long receiverScore;
    private Long winner;
}
