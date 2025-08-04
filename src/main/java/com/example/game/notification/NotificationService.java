package com.example.game.notification;

import com.example.game.enums.NotificationState;
import com.example.game.exceptions.NotFoundException;
import com.example.game.exceptions.UnauthorizedException;
import com.example.game.payloads.entities.NotificationDTO;
import com.example.game.socket.chat.Chat;
import com.example.game.socket.chat.ChatService;
import com.example.game.user.User;
import com.example.game.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ChatService chatService;
    private final UserService userService;
    public Notification save(NotificationDTO notificationDTO){
        Chat chat = chatService.findById(notificationDTO.chatId());
        User sender = userService.findById(notificationDTO.senderId());
        User receiver = userService.findById(notificationDTO.receiverId());
        var testo = "";
        if(chat.getUtenti().size()>2 && notificationDTO.testo() == null){
            testo = "Controlla la chat " + chat.getTitle() + "! Hai dei nuovi messaggi.";
        }else if(chat.getUtenti().size() == 2 && notificationDTO.testo() == null){
            testo = "Controlla la chat con " + chat.getUtenti().stream().filter(u->u.getId()!=receiver.getId()).toList().get(0).getFullName() + "! Hai dei nuovi messaggi.";
        }else {
            testo = notificationDTO.testo();
        }
        return notificationRepository.save(Notification.builder().state(NotificationState.SENT).testo(testo).chat(chat).receiver(receiver)
                .sender(sender).createdAtDate(LocalDate.now()).createdAt(LocalDate.now().toString()).isActive(true)
                .modifiedAt(LocalDate.now().toString()).build());
    }

    public Notification read(Long notificationId, User user){
        Notification notification = findById(notificationId);
        if(user.getId() == notification.getReceiver().getId()){
            notification.setState(NotificationState.READ);
            notification.setModifiedAt(LocalDate.now().toString());
            return  notificationRepository.save(notification);
        }
        throw new UnauthorizedException("Non puoi modificare questa notifica");
    }

    public Notification findById(Long notificationId){
        return notificationRepository.findById(notificationId).orElseThrow(()-> new NotFoundException("Notifica non trovata."));
    }
}
