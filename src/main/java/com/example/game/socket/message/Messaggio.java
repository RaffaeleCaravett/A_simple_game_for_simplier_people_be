package com.example.game.socket.message;

import com.example.game.entityInfos.EntityInfos;
import com.example.game.enums.MessageState;
import com.example.game.socket.chat.Chat;
import com.example.game.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Messaggio extends EntityInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Chat chat;
    @ManyToOne(fetch = FetchType.EAGER)
    private User sender;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "messaggi_users",
            joinColumns = @JoinColumn(name = "messaggio_id"),
            inverseJoinColumns = @JoinColumn(name = "ricevitore_id"))
    private List<User> receivers;
    @Enumerated(EnumType.STRING)
    private MessageState state;
    private String text;
    private Long settedChatId;


    public Long getSettedChatId() {
        return chat.getId();
    }
    public List<Long> getReceivers(){
        return this.receivers.stream().map(User::getId).toList();
    }
}
