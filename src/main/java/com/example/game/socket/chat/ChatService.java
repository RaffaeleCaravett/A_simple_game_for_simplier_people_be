package com.example.game.socket.chat;

import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.ChatDTO;
import com.example.game.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserService userService;

    public Chat findById(Long id) {
        return chatRepository.findById(id).orElseThrow(() -> new BadRequestException("Chat non trovata."));
    }

    public List<Chat> findAll(Long userId, Boolean active) {
        return chatRepository.findAll(Specification.where(ChatRepository.userIdEqual(userId))
                .and(ChatRepository.isActive(active)));
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

    public Chat save(ChatDTO chatDTO) {
        Chat chat = new Chat().builder()
                .createdAt(LocalDate.now().toString())
                .createdAtDate(LocalDate.now())
                .modifiedAt(LocalDate.now().toString())
                .isActive(true)
                .utenti(chatDTO.userId().stream().map(userService::findById).collect(Collectors.toSet()).stream().toList())
                .build();
        return chatRepository.save(chat);
    }
}
