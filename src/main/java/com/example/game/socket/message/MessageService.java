package com.example.game.socket.message;

import com.example.game.enums.MessageState;
import com.example.game.enums.Role;
import com.example.game.exceptions.BadRequestException;
import com.example.game.exceptions.UnauthorizedException;
import com.example.game.payloads.entities.MessageDTO;
import com.example.game.socket.chat.Chat;
import com.example.game.socket.chat.ChatService;
import com.example.game.user.User;
import com.example.game.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final UserService userService;

    public Messaggio save(MessageDTO messageDTO) {

        Chat chat = chatService.findById(messageDTO.chat());
        User sender = userService.findById(messageDTO.mittente());
        List<User> receivers = messageDTO.riceventi().stream().map(userService::findById).collect(Collectors.toSet()).stream().toList();
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

    public boolean delete(Long id,User user){
        Messaggio messaggio = findById(id);
        if(messaggio.getSender().getId()==user.getId() || user.getRole().equals(Role.Admin)){
           messaggio.setActive(false);
           messaggio.setDeletedAt(LocalDate.now().toString());
           messageRepository.save(messaggio);
           return true;
        }else{
            throw new UnauthorizedException("Non sei autorizzato a modificare questo messaggio");
        }
    }

    public Messaggio findById(Long id) {
        return messageRepository.findById(id).orElseThrow(() -> new BadRequestException("Messaggio non trovato"));
    }
}
