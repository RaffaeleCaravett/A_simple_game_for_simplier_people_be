package com.example.game.socket.connection;

import com.example.game.blocked.Blocked;
import com.example.game.enums.*;
import com.example.game.exceptions.BadRequestException;
import com.example.game.gioco.Gioco;
import com.example.game.gioco.GiocoService;
import com.example.game.invito.Invito;
import com.example.game.invito.InvitoService;
import com.example.game.notification.Notification;
import com.example.game.notification.NotificationRepository;
import com.example.game.partitaDouble.PartitaDouble;
import com.example.game.partitaDouble.PartitaDoubleService;
import com.example.game.payloads.entities.MoveToHandleDTO;
import com.example.game.payloads.entities.SocketDTO;
import com.example.game.socket.chat.Chat;
import com.example.game.socket.chat.ChatService;
import com.example.game.socket.message.MessageRepository;
import com.example.game.socket.message.Messaggio;
import com.example.game.socket.message.messageImage.MessageImageRepository;
import com.example.game.tournament.TournamentService;
import com.example.game.user.User;
import com.example.game.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin
@RequiredArgsConstructor
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
    @Autowired
    private GiocoService giocoService;
    private final MessageImageRepository messageImageRepository;
    private final TournamentService tournamentService;
    private final PartitaDoubleService partitaDoubleService;

    @MessageMapping("/send")
    @SendTo("/updates/receive")
    public Object addMessage(@RequestBody SocketDTO socketDTO) throws InterruptedException, IOException {
        var stompType = socketDTO.messageDTO() != null ? StompType.MESSAGE :
                socketDTO.connectionDTO() != null ? StompType.CONNECTION :
                        socketDTO.gameConnectionDTO() != null ? StompType.GAME_CONNECTION :
                                socketDTO.moveDTO() != null ? StompType.MOVE : socketDTO.connectionRequestDTO() != null ?
                                        StompType.CONNECTION_REQUEST : socketDTO.notification() != null ? StompType.TOURNAMENT : socketDTO.invitoDTO() != null ? StompType.INVITO : "";
        if (stompType.equals(StompType.MOVE)) {
            if (socketDTO.moveDTO() == null) {
                throw new BadRequestException("Impossibile determinare il tipo di mossa");
            }
            Invito invito = invitoService.findById(socketDTO.moveDTO().invitationId());
            if (socketDTO.moveDTO().moveType().equals(MoveType.INVITE)) {
                try {
                    return MoveToHandleDTO.builder().giocoId(invito.getGioco().getId()).inviteState(invito.getInviteState())
                            .senderId(invito.getSender().getId()).build();
                } catch (Exception e) {
                    throw new BadRequestException("Invito non valito");
                }
            } else if (socketDTO.moveDTO().moveType().equals(MoveType.START)) {
                return MoveToHandleDTO.builder().giocoId(invito.getGioco().getId()).inviteState(InviteState.START)
                        .senderId(invito.getSender().getId());
            } else if (MoveType.COMPLETED.equals(socketDTO.moveDTO().moveType())) {
                return MoveToHandleDTO.builder().giocoId(invito.getGioco().getId()).inviteState(InviteState.END)
                        .senderId(invito.getSender().getId())
                        .senderScore(socketDTO.moveDTO().senderScore()).receiverScore(socketDTO.moveDTO().oppositeScore());
            } else if (MoveType.TIMEOUT.equals(socketDTO.moveDTO().moveType())) {
                return MoveToHandleDTO.builder().giocoId(invito.getGioco().getId()).inviteState(InviteState.END)
                        .senderId(invito.getSender().getId())
                        .senderScore(socketDTO.moveDTO().senderScore()).receiverScore(socketDTO.moveDTO().oppositeScore());
            } else if (MoveType.MOVE.equals(socketDTO.moveDTO().moveType())) {
                return MoveToHandleDTO.builder().giocoId(invito.getGioco().getId()).inviteState(InviteState.MOVE)
                        .senderId(invito.getSender().getId()).moverId(socketDTO.moveDTO().moverId());
            } else {
                throw new BadRequestException("Mossa non determinabile");
            }
        } else if (stompType.equals(StompType.MESSAGE)) {
            if (socketDTO.messageDTO() == null) {
                throw new BadRequestException("Impossibile salvare e inviare il messaggio");
            }

            User sender = userService.findById(socketDTO.messageDTO().mittente());
            Chat chat = chatService.findById(socketDTO.messageDTO().chat());
            List<User> receivers = socketDTO.messageDTO().riceventi().stream().map(userService::findById).collect(Collectors.toSet()).stream().toList();
            Messaggio messaggio = Messaggio.builder().createdAt(LocalDate.now().toString()).createdAtDate(LocalDate.now()).chat(
                            chat).isActive(true).state(MessageState.SENT).text(socketDTO.messageDTO().message())
                    .sender(sender).receivers(receivers).build();
            List<User> users = messaggio.getReceivers().stream().map(userService::findById).toList();
            users.forEach(u -> {
                if (!u.getIsConnected()) {
                    notificationRepository.save(Notification.builder().testo("Hai un nuovo messaggio da " + messaggio.getSender().getFullName())
                            .sender(messaggio.getSender()).receiver(u).chat(messaggio.getChat()).state(NotificationState.SENT).createdAtDate(LocalDate.now())
                            .createdAt(LocalDate.now().toString()).notificationType(NotificationType.MESSAGE).build());
                }
            });
            if (chat.getChatType().equals(ChatType.SINGOLA) &&
                    (sender.getBlockeds().stream().map(Blocked::getBlocked).map(User::getId).toList().contains(receivers.get(0).getId())
                            || receivers.get(0).getBlockeds().stream().map(Blocked::getBlocked).map(User::getId).toList().contains(sender.getId()))) {
                return null;
            }
            messageRepository.save(messaggio);
            return messaggio;
        } else if (stompType.equals(StompType.CONNECTION)) {
            if (socketDTO.connectionDTO() == null) {
                throw new BadRequestException("Impossibile stabilire quale utente si sia connesso.");
            }
            //User user=  userService.findById(socketDTO.connectionDTO().getUserId());
            User user = userService.findById(socketDTO.connectionDTO().getUserId());
            Thread.sleep(3000);
            return user;
        } else if (stompType.equals(StompType.GAME_CONNECTION)) {
            if (socketDTO.gameConnectionDTO() == null) {
                throw new BadRequestException("Impossibile determinare a che gioco ci si è connessi.");
            }
            Gioco gioco = giocoService.findById(socketDTO.gameConnectionDTO().getGiocoId());
            User user = userService.findById(socketDTO.gameConnectionDTO().getUserId());
            return MoveToHandleDTO.builder().connection(socketDTO.gameConnectionDTO().getConnected()).connectionMove(true)
                    .giocoConnection(gioco.getId()).userConnected(user.getId());
        } else if (stompType.equals(StompType.CONNECTION_REQUEST)) {
            if (socketDTO.connectionRequestDTO() == null) {
                throw new BadRequestException("Impossibile determinare a che gioco ci si è connessi.");
            }
            User user = userService.findById(socketDTO.connectionRequestDTO().receiverId());
            return MoveToHandleDTO.builder().inviteState(InviteState.CONNECTION_REQUEST).receiverId(user.getId()).build();
        } else if (stompType.equals(StompType.TOURNAMENT)) {
            if (socketDTO.notification() == null) {
                throw new BadRequestException("Impossibile stabilire il tipo di notifica");
            }
            return socketDTO.notification();
        } else if (stompType.equals(StompType.INVITO)) {
            if (null == socketDTO.invitoDTO().status()) {
                throw new BadRequestException("Lo stato dell'invito non può essere vuoto");
            }
            Boolean inviteState = socketDTO.invitoDTO().status().equals("ACCETTATO");
            if (inviteState) {
                var invito = socketDTO.invitoDTO();
                var invitoEntity = invitoService.findById(invito.invitoId());
                var partecipanti = new HashSet<User>();
                partecipanti.add(userService.findById(invito.senderId()));
                partecipanti.add(userService.findById(invito.accepterId()));
                invitoEntity.setInviteState(InviteState.ACCEPTED);
                invitoService.persist(invitoEntity);
                Gioco gioco = giocoService.findById(invito.giocoId());
                return partitaDoubleService.save(PartitaDouble.builder().gioco(gioco)
                        .tournament(null != invito.torneo() ? tournamentService.findById(invito.torneo()) : null)
                        .invito(invitoService.findById(invito.invitoId())).isActive(true).createdAt(LocalDate.now().toString())
                        .createdAtDate(LocalDate.now()).partecipanti(partecipanti).build());
            }
        }
        throw new BadRequestException("Impossibile determinare l'azione!");
    }
}
