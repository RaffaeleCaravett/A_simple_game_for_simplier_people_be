package com.example.game.socket.chat;

import com.example.game.enums.MessageState;
import com.example.game.enums.Role;
import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.ChatDTO;
import com.example.game.payloads.entities.MessageDTO;
import com.example.game.socket.message.MessageRepository;
import com.example.game.socket.message.Messaggio;
import com.example.game.user.User;
import com.example.game.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    //@Autowired
    //private SimpMessagingTemplate template;

    @Autowired
    private MessageRepository messageRepository;
    private final ChatService chatService;
    private final UserService userService;

    /**
     * Sends a message to its destination channel
     *
     * @param message
     * @MessageMapping("/messages") public void handleMessage(@RequestBody @Valid MessageDTO message, BindingResult bindingResult) {
     * if (bindingResult.hasErrors()) throw new BadRequestException(bindingResult.getAllErrors());
     * <p>
     * //        template.convertAndSend("/channel/chat/" + message.chat(), message);
     * Messaggio messaggio = new Messaggio();
     * messaggio.setCreatedAtDate(LocalDate.now());
     * messaggio.setCreatedAt(LocalDate.now().toString());
     * messaggio.setChat(chatService.findById(message.chat()));
     * messaggio.setSender(userService.findById(message.mittente()));
     * messaggio.setReceivers(message.riceventi().stream().map(userService::findById).collect(Collectors.toSet()).stream().toList());
     * messaggio.setState(MessageState.SENT);
     * messaggio.setText(message.message());
     * messaggio.setModifiedAt(LocalDate.now().toString());
     * messageRepository.save(messaggio);
     * }
     */

    @PostMapping("")
    public Chat post(@RequestPart(value = "file", required = false) MultipartFile file, @RequestPart("chat") @Valid ChatDTO chatDTO, BindingResult bindingResult, @AuthenticationPrincipal User user) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return chatService.save(chatDTO, user,file);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public boolean delete(@PathVariable Long id) {
        return chatService.deleteChat(id);
    }

    @GetMapping("/params")
    public List<Chat> findByTitleContaining(@RequestParam(required = false) String title, @RequestParam Long userId, @RequestParam(required = false, defaultValue = "true") Boolean isActive, @AuthenticationPrincipal User user) {
        var userIdentityNumber = 0L;
        if (user.getRole().equals(Role.Admin)) userIdentityNumber = userId;
        else userIdentityNumber = user.getId();
        return chatService.findByParams(title, userIdentityNumber, isActive);
    }

    @GetMapping("/availableContacts")
    public Set<User> getAvailableContacts(@AuthenticationPrincipal User user){
        return this.chatService.getAvailableContacts(user);
    }
}