package com.example.game.socket.chat;

import com.example.game.entityInfos.EntityInfos;
import com.example.game.socket.message.Messaggio;
import com.example.game.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class Chat extends EntityInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "chats_utenti",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "utente_id"))
    private List<User> utenti;
    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Messaggio> messaggi;


    public List<Messaggio> getMessaggi() {
        return this.messaggi.stream().filter(Messaggio::isActive).collect(Collectors.toSet()).stream().toList();
    }
}
