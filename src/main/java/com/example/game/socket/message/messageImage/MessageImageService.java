package com.example.game.socket.message.messageImage;


import com.example.game.socket.message.MessageService;
import com.example.game.socket.message.Messaggio;
import com.example.game.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Validated
@RequiredArgsConstructor
public class MessageImageService {

    private final MessageService messageService;
    private final MessageImageRepository messageImageRepository;

    public MessageImage save(MultipartFile multipartFile, Long messaggioId, User user) throws IOException {
        Messaggio messaggio = messageService.findById(messaggioId);
        MessageImage messageImage = MessageImage.builder()
                .image(multipartFile.getBytes())
                .name(multipartFile.getOriginalFilename())
                .messaggio(messaggio).build();
        return messageImageRepository.save(messageImage);
    }
}
