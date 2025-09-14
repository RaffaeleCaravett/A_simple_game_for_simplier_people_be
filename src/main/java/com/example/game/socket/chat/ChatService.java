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

    public boolean deleteChat(Long id, User user) {
        try {
            Chat chat = findById(id);
            if (chat.getChatType().equals(ChatType.GRUPPO) && chat.getAdministrators().stream().map(User::getId).toList().contains(user.getId())) {
                chat.setActive(false);
                chat.setDeletedAt(LocalDate.now().toString());
            } else {
                User user1 = chat.getUtenti().stream().filter(u -> u.getId() == user.getId()).toList().get(0);
                var chats = new ArrayList<>(user1.getChats().stream().filter(c -> !c.getId().equals(chat.getId())).toList());
                user1.setChats(chats);
                var users = new ArrayList<>(chat.getUtenti().stream().filter(u -> u.getId() != user1.getId()).toList());
                chat.setUtenti(users);
                userService.save(user1);
            }
            chatRepository.save(chat);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Chat leaveChat(Long id, User user) {
        Chat chat = findById(id);
        if (chat.getChatType().equals(ChatType.GRUPPO) && chat.getUtenti().stream().map(User::getId).toList().contains(user.getId())) {
            var users = new ArrayList<>(chat.getUtenti().stream().filter(u -> u.getId() != user.getId()).toList());
            chat.setUtenti(users);
            var chats = new ArrayList<>(user.getChats().stream().filter(c -> !c.getId().equals(chat.getId())).toList());
            user.setChats(chats);
            if (chat.getAdministrators().stream().map(User::getId).toList().contains(user.getId())) {
                var admins = new ArrayList<>(chat.getAdministrators().stream().filter(u -> u.getId() != user.getId()).toList());
                chat.setAdministrators(admins);
                if (chat.getAdministrators().isEmpty()) {
                    var userAdmins = chat.getUtenti();
                    chat.setAdministrators(userAdmins);
                }
            }
            userService.save(user);
            return chatRepository.save(chat);
        } else {
            throw new BadRequestException("Non puoi abbandonare una chat di cui non sei partecipante");
        }
    }

    public Chat save(ChatDTO chatDTO, User user, MultipartFile file) throws IOException {
        List<User> administrators = new ArrayList<>();
        administrators.add(user);
        Chat chat = new Chat().builder()
                .createdAt(LocalDate.now().toString())
                .createdAtDate(LocalDate.now())
                .modifiedAt(LocalDate.now().toString())
                .isActive(true)
                .utenti(chatDTO.userId().stream().map(userService::findById).collect(Collectors.toSet()).stream().toList())
                .title(chatDTO.title())
                .chatType(chatDTO.chatType() != null ? ChatType.valueOf(chatDTO.chatType()) : null)
                .build();
        if (chat.getChatType().equals(ChatType.GRUPPO)) {
            chat.setAdministrators(administrators);
        }
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
            optionsArray.add("Blocca " + chat.getUtenti().stream().filter(u -> u.getId() != user.getId()).toList().get(0).getNome());
            optionsArray.add("Elimina chat");
            return ChatOptionsMenuDTO.builder().options(optionsArray).build();
        } else {
            optionsArray.add("Aggiungi partecipante");
            optionsArray.add("Aggiungi/rimuovi un admin");
            optionsArray.add("Cambia foto");
            optionsArray.add("Elimina chat");
            optionsArray.add("Abbandona gruppo");
            return ChatOptionsMenuDTO.builder().options(optionsArray).build();
        }
    }

    public Chat patch(Long chatId, ChatDTO chatDTO) {
        Chat chat = findById(chatId);
        if (chatDTO.title() != null) {
            chat.setTitle(chatDTO.title());
        }
        if (chatDTO.chatType() != null) {
            chat.setChatType(ChatType.valueOf(chatDTO.chatType()));
        }
        List<User> users = new ArrayList<>();
        for (Long i : chatDTO.userId()) {
            users.add(userService.findById(i));
        }
        if (null != chatDTO.administrators() && !chatDTO.administrators().isEmpty()) {
            List<User> administrators = new ArrayList<>();
            for (Long l : chatDTO.administrators()) {
                administrators.add(userService.findById(l));
            }
            if (!new HashSet<>(chat.getAdministrators().stream().map(User::getId).toList()).containsAll(chatDTO.administrators())) {
                chat.setAdministrators(administrators);
            }
        }
        chat.setUtenti(users);
        return chatRepository.save(chat);
    }

    public Chat setChatImage(User user, Long id, MultipartFile multipartFile) throws IOException {
        Chat chat = findById(id);
        if (chat.getChatType().equals(ChatType.GRUPPO) && chat.getAdministrators().stream().map(User::getId).toList().contains(user.getId())) {
            chat.setImage(multipartFile.getBytes());
            return chatRepository.save(chat);
        } else {
            throw new BadRequestException("Impossibile modificare l'immagine");
        }
    }
}
