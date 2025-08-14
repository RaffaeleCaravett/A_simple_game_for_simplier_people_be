package com.example.game.socket.connection;

import com.example.game.enums.*;
import com.example.game.exceptions.BadRequestException;
import com.example.game.invito.Invito;
import com.example.game.invito.InvitoService;
import com.example.game.notification.Notification;
import com.example.game.notification.NotificationRepository;
import com.example.game.payloads.entities.MessageDTO;
import com.example.game.payloads.entities.MoveDTO;
import com.example.game.payloads.entities.MoveToHandleDTO;
import com.example.game.payloads.entities.SocketDTO;
import com.example.game.socket.chat.ChatService;
import com.example.game.socket.message.MessageRepository;
import com.example.game.socket.message.Messaggio;
import com.example.game.user.User;
import com.example.game.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin
public class ConnectionController {

    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private InvitoService invitoService;

    @MessageMapping("/send")
    @SendTo("/updates/receive")
    public Object addMessage(@RequestBody SocketDTO socketDTO, @RequestParam StompType stompType) {
        switch (stompType) {
            case MOVE -> {
                if (socketDTO.moveDTO() == null) {
                    throw new BadRequestException("Impossibile determinare il tipo di mossa");
                }
                Invito invito = invitoService.findById(socketDTO.moveDTO().invitationId());
                if (socketDTO.moveDTO().moveType().equals(MoveType.INVITE)) {
                    try {
                        return MoveToHandleDTO.builder().giocoId(invito.getGioco().getId()).inviteState(invito.getInviteState()).receiverId(invito.getReceiver().getId())
                                .senderId(invito.getSender().getId()).build();
                    } catch (Exception e) {
                        throw new BadRequestException("Invito non valito");
                    }
                } else if (socketDTO.moveDTO().moveType().equals(MoveType.START)) {
                    return MoveToHandleDTO.builder().giocoId(invito.getGioco().getId()).inviteState(InviteState.START)
                            .senderId(invito.getSender().getId()).receiverId(invito.getReceiver().getId());
                } else if (MoveType.COMPLETED.equals(socketDTO.moveDTO().moveType())) {
                    return MoveToHandleDTO.builder().giocoId(invito.getGioco().getId()).inviteState(InviteState.END)
                            .senderId(invito.getSender().getId()).receiverId(invito.getReceiver().getId())
                            .senderScore(socketDTO.moveDTO().senderScore()).receiverScore(socketDTO.moveDTO().oppositeScore());
                } else if (MoveType.TIMEOUT.equals(socketDTO.moveDTO().moveType())) {
                    return MoveToHandleDTO.builder().giocoId(invito.getGioco().getId()).inviteState(InviteState.END)
                            .senderId(invito.getSender().getId()).receiverId(invito.getReceiver().getId())
                            .senderScore(socketDTO.moveDTO().senderScore()).receiverScore(socketDTO.moveDTO().oppositeScore())
                            .winner(invito.getSender().getId() == socketDTO.moveDTO().userTimeoutId() ? invito.getReceiver().getId() : invito.getSender().getId());
                }else if(MoveType.MOVE.equals(socketDTO.moveDTO().moveType())){
                    return MoveToHandleDTO.builder().giocoId(invito.getGioco().getId()).inviteState(InviteState.MOVE)
                            .senderId(invito.getSender().getId()).receiverId(invito.getReceiver().getId()).moverId(socketDTO.moveDTO().moverId());
                }else{
                    throw new BadRequestException("Mossa non determinabile");
                }
            }
            case MESSAGE -> {
                if (socketDTO.messageDTO() == null) {
                    throw new BadRequestException("Impossibile salvare e inviare il messaggio");
                }
                Messaggio messaggio = Messaggio.builder().createdAt(LocalDate.now().toString()).createdAtDate(LocalDate.now()).chat(
                                chatService.findById(socketDTO.messageDTO().chat())).isActive(true).state(MessageState.SENT).text(socketDTO.messageDTO().message())
                        .sender(userService.findById(socketDTO.messageDTO().mittente())).receivers(socketDTO.messageDTO().riceventi().stream().map(userService::findById).collect(Collectors.toSet()).stream().toList()).build();
                List<User> users = messaggio.getReceivers().stream().map(userService::findById).toList();
                users.forEach(u -> {
                    if (!u.getIsConnected()) {
                        notificationRepository.save(Notification.builder().testo("Hai un nuovo messaggio da " + messaggio.getSender().getFullName())
                                .sender(messaggio.getSender()).receiver(u).chat(messaggio.getChat()).state(NotificationState.SENT).createdAtDate(LocalDate.now())
                                .createdAt(LocalDate.now().toString()).build());
                    }
                });
                return messageRepository.save(messaggio);
            }
            case CONNECTION -> {
                break;
            }
        }
        return null;
    }
}
