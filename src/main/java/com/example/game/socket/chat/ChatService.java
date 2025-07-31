package com.example.game.socket.chat;

import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.ChatDTO;
import com.example.game.user.User;
import com.example.game.user.UserService;
import lombok.RequiredArgsConstructor;
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

    public Chat save(ChatDTO chatDTO, User user) {
        Chat chat = new Chat().builder()
                .createdAt(LocalDate.now().toString())
                .createdAtDate(LocalDate.now())
                .modifiedAt(LocalDate.now().toString())
                .isActive(true)
                .utenti(chatDTO.userId().stream().map(userService::findById).collect(Collectors.toSet()).stream().toList())
                .title(chatDTO.title())
                .build();
        return chatRepository.save(chat);
    }

}
