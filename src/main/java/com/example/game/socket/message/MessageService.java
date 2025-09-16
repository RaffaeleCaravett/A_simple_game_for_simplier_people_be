package com.example.game.socket.message;

import com.example.game.blocked.Blocked;
import com.example.game.enums.ChatType;
import com.example.game.enums.MessageState;
import com.example.game.enums.Role;
import com.example.game.exceptions.BadRequestException;
import com.example.game.exceptions.UnauthorizedException;
import com.example.game.payloads.entities.MessageDTO;
import com.example.game.socket.chat.Chat;
import com.example.game.socket.chat.ChatService;
import com.example.game.user.User;
import com.example.game.user.UserService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final UserService userService;

    //@Autowired
    //private SimpMessagingTemplate template;
    public Messaggio save(MessageDTO messageDTO) {
        //  template.convertAndSend("/channel/chat/" + messageDTO.chat(), messageDTO);

        Chat chat = chatService.findById(messageDTO.chat());
        User sender = userService.findById(messageDTO.mittente());
        List<User> receivers = messageDTO.riceventi().stream().map(userService::findById).collect(Collectors.toSet()).stream().toList();
        if (chat.getChatType().equals(ChatType.SINGOLA) && sender.getBlockeds().stream().map(Blocked::getBlocked).map(User::getId).toList().contains(receivers.get(0).getId())) {
            return null;
        }
        Messaggio messaggio = Messaggio.builder().state(MessageState.SENT).chat(chat)
                .receivers(receivers).sender(sender).isActive(true).modifiedAt(LocalDate.now().toString())
                .createdAt(LocalDate.now().toString()).createdAtDate(LocalDate.now()).text(messageDTO.message()).build();

        return messageRepository.save(messaggio);
    }

    public Messaggio modify(Long id, MessageDTO messageDTO, User user) {
        Messaggio messaggio = findById(id);
        if (messaggio.getSender().getId() == user.getId() || user.getRole().equals(Role.Admin)) {
            messaggio.setText(messageDTO.message());
            messaggio.setModifiedAt(LocalDate.now().toString());
            return messageRepository.save(messaggio);
        } else {
            throw new UnauthorizedException("Non sei autorizzato a modificare questo messaggio");
        }
    }

    public boolean delete(Long id, User user) {
        Messaggio messaggio = findById(id);
        if (messaggio.getSender().getId() == user.getId() || user.getRole().equals(Role.Admin)) {
            messaggio.setActive(false);
            messaggio.setDeletedAt(LocalDate.now().toString());
            messageRepository.save(messaggio);
            return true;
        } else {
            throw new UnauthorizedException("Non sei autorizzato a modificare questo messaggio");
        }
    }

    public Messaggio findById(Long id) {
        return messageRepository.findById(id).orElseThrow(() -> new BadRequestException("Messaggio non trovato"));
    }

    public List<Messaggio> findAll(Long chatId, Long userId, @Nullable MessageState messageState) {
        return messageRepository.findAll(Specification.where(MessageRepository.chatIdEquals(chatId))
                .and(MessageRepository.receiversContain(userId))
                .and(MessageRepository.stateEquals(messageState)));
    }

    public boolean read(Long chatId, User user) {
        try {
            List<Messaggio> messaggi = findAll(chatId, user.getId(), MessageState.SENT);
            messaggi.forEach(m -> {
                if (CollectionUtils.isEmpty(m.getReaders())) {
                    List<User> users = new ArrayList<>();
                    users.add(user);
                    m.setReaders(users);
                    m.setState(MessageState.READ);
                } else if (!m.getReaders().contains(user.getId())) {
                    var readers = m.getReaders();
                    readers.add(user.getId());
                    m.setReaders(readers.stream().map(userService::findById).toList());
                    m.setState(MessageState.READ);
                }
                messageRepository.save(m);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
