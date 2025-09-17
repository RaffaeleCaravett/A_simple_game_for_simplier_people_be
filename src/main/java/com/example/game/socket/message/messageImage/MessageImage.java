package com.example.game.socket.message.messageImage;

import com.example.game.socket.message.Messaggio;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "message_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageImage {
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
