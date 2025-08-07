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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ChatService chatService;
    private final UserService userService;

    public Notification save(NotificationDTO notificationDTO) {
        Chat chat = chatService.findById(notificationDTO.chatId());
        User sender = userService.findById(notificationDTO.senderId());
        User receiver = userService.findById(notificationDTO.receiverId());
        var testo = "";
        if (chat.getUtenti().size() > 2 && notificationDTO.testo() == null) {
            testo = "Controlla la chat " + chat.getTitle() + "! Hai dei nuovi messaggi.";
        } else if (chat.getUtenti().size() == 2 && notificationDTO.testo() == null) {
            testo = "Controlla la chat con " + chat.getUtenti().stream().filter(u -> u.getId() != receiver.getId()).toList().get(0).getFullName() + "! Hai dei nuovi messaggi.";
        } else {
            testo = notificationDTO.testo();
        }
        return notificationRepository.save(Notification.builder().state(NotificationState.SENT).testo(testo).chat(chat).receiver(receiver)
                .sender(sender).createdAtDate(LocalDate.now()).createdAt(LocalDate.now().toString()).isActive(true)
                .modifiedAt(LocalDate.now().toString()).build());
    }

    public boolean read(List<Long> notificationId, User user) {
        List<Notification> notifications = notificationRepository.findAllById(notificationId);
        notifications.forEach(notification -> {
            if (user.getId() == notification.getReceiver().getId()) {
                notification.setState(NotificationState.READ);
                notification.setModifiedAt(LocalDate.now().toString());
                notificationRepository.save(notification);
            } else {
                throw new UnauthorizedException("Non puoi modificare questa notifica");
            }
        });
        return true;
    }

    public Notification findById(Long notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow(() -> new NotFoundException("Notifica non trovata."));
    }

    public List<Notification> getAllByUserId(Long userId) {
        var list = notificationRepository.findAll(Specification.where(NotificationRepository.userId(userId)));
        list.sort(Comparator.comparing(Notification::getId).reversed());
        return list;
    }
}
