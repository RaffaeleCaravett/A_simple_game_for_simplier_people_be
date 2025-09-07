package com.example.game.socket.chat;

import com.example.game.connectionRequest.ConnectionRequest;
import com.example.game.connectionRequest.ConnectionRequestService;
import com.example.game.enums.ChatType;
import com.example.game.enums.EsitoRichiesta;
import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.ChatDTO;
import com.example.game.payloads.entities.ChatOptionsMenuDTO;
import com.example.game.user.User;
import com.example.game.user.UserService;
import jakarta.mail.search.SearchTerm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserService userService;
    private final ConnectionRequestService connectionRequestService;

    public Chat findById(Long id) {
        return chatRepository.findById(id).orElseThrow(() -> new BadRequestException("Chat non trovata."));
    }

    public List<Chat> findByParams(String title, Long userId, Boolean active) {
        User user = userService.findById(userId);
        return chatRepository.findAll(Specification.where(ChatRepository.userIdEqual(userId))
                .and(ChatRepository.titleLike(title))
                .and(ChatRepository.isActive(active))).stream().map(chat -> {
            if (null == chat.getTitle() || chat.getTitle().isEmpty()) {
                chat.setTitle(chat.getUtenti().stream().filter(u -> !u.getEmail().equals(user.getEmail())).collect(Collectors.toSet()).stream().toList().get(0).getFullName());
            }
            return chat;
        }).toList();
    }

    public boolean deleteChat(Long id) {
        try {
            Chat chat = findById(id);
            chat.setActive(false);
            chatRepository.save(chat);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Chat save(ChatDTO chatDTO, User user, MultipartFile file) throws IOException {
        Chat chat = new Chat().builder()
                .createdAt(LocalDate.now().toString())
                .createdAtDate(LocalDate.now())
                .modifiedAt(LocalDate.now().toString())
                .isActive(true)
                .utenti(chatDTO.userId().stream().map(userService::findById).collect(Collectors.toSet()).stream().toList())
                .title(chatDTO.title())
                .chatType(chatDTO.chatType() != null ? ChatType.valueOf(chatDTO.chatType()) : null)
                .build();

        if (file != null && !file.isEmpty()) {
            byte[] fileBytes = file.getBytes();
            chat.setImage(fileBytes);
        }
        return chatRepository.save(chat);
    }

    public Set<User> getAvailableContacts(User user) {
        List<ConnectionRequest> connectionsSent = connectionRequestService.findAllByParams(user.getId(), null, "ACCETTATA");
        List<ConnectionRequest> connectionsReceived = connectionRequestService.findAllByParams(null, user.getId(), "ACCETTATA");

        List<User> availableUsers = new ArrayList<>();
        if (!CollectionUtils.isEmpty(connectionsSent)) {
            availableUsers.addAll(connectionsSent.stream().map(ConnectionRequest::getReceiver).collect(Collectors.toSet()));
        }
        if (!CollectionUtils.isEmpty(connectionsReceived)) {
            availableUsers.addAll(connectionsReceived.stream().map(ConnectionRequest::getSender).collect(Collectors.toSet()));
        }
        if (!CollectionUtils.isEmpty(availableUsers)) {
            return new HashSet<>(availableUsers);
        }
        return new HashSet<>();
    }

    public ChatOptionsMenuDTO getChatOptionsMenu(Long id, User user) {
        Chat chat = findById(id);
        List<String> optionsArray = new ArrayList<>();
        optionsArray.add("Info chat");

        if (chat.getChatType().equals(ChatType.SINGOLA)) {
            optionsArray.add("Blocca " + chat.getUtenti().stream().filter(u -> u.getId() != user.getId()).toList().get(0).getFullName());
            optionsArray.add("Elimina chat");
            return ChatOptionsMenuDTO.builder().options(optionsArray).build();
        } else {
            optionsArray.add("Aggiungi partecipante");
            optionsArray.add("Cambia foto");
            return ChatOptionsMenuDTO.builder().options(optionsArray).build();
        }
    }
}
