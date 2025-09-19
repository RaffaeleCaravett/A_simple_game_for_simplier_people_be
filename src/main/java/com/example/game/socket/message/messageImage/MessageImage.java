package com.example.game.socket.message.messageImage;

import com.example.game.entityInfos.EntityInfos;
import com.example.game.socket.message.Messaggio;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "message_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MessageImage extends EntityInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private byte[] image;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "messaggio_id")
    @JsonIgnore
    private Messaggio messaggio;
}
