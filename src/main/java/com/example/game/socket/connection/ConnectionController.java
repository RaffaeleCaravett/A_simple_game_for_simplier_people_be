package com.example.game.socket.connection;

import com.example.game.enums.MessageState;
import com.example.game.payloads.entities.MessageDTO;
import com.example.game.socket.chat.ChatService;
import com.example.game.socket.message.MessageRepository;
import com.example.game.socket.message.Messaggio;
import com.example.game.user.User;
import com.example.game.user.UserRepository;
import com.example.game.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @MessageMapping("/messages")
    @SendTo("/messages/receive")
    public Messaggio addMessage(@RequestBody MessageDTO messageDTO) {
        return messageRepository.save(
                Messaggio.builder().createdAt(LocalDate.now().toString()).createdAtDate(LocalDate.now()).chat(
                                chatService.findById(messageDTO.chat())).isActive(true).state(MessageState.SENT).text(messageDTO.message())
                        .sender(userService.findById(messageDTO.mittente())).receivers(messageDTO.riceventi().stream().map(userService::findById).collect(Collectors.toSet()).stream().toList()).build()
        );
    }

}
