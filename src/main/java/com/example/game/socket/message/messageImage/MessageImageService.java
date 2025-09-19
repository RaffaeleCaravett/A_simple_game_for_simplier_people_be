package com.example.game.socket.message.messageImage;


import com.example.game.exceptions.BadRequestException;
import com.example.game.exceptions.NotFoundException;
import com.example.game.exceptions.UnauthorizedException;
import com.example.game.socket.message.MessageService;
import com.example.game.socket.message.Messaggio;
import com.example.game.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Validated
@RequiredArgsConstructor
public class MessageImageService {

    private final MessageService messageService;
    private final MessageImageRepository messageImageRepository;

    public MessageImage save(MultipartFile multipartFile, Long messaggioId, User user) throws IOException {
        Messaggio messaggio = messageService.findById(messaggioId);
        if (messaggio.getSender().getId() == user.getId()) {
            MessageImage messageImage = MessageImage.builder()
                    .image(multipartFile.getBytes())
                    .name(multipartFile.getOriginalFilename())
                    .messaggio(messaggio)
                    .createdAt(LocalDate.now().toString())
                    .createdAtDate(LocalDate.now())
                    .isActive(true).build();
            return messageImageRepository.save(messageImage);
        } else {
            throw new UnauthorizedException("Non hai i permessi per salvare questa immagine");
        }
    }

    public boolean deleteImageMessage(Long id, User user) {
        List<Messaggio> messaggio = messageService.findAll(null, null, null, id);
        if (messaggio.isEmpty()) {
            throw new NotFoundException("Messaggio non trovato");
        }
        Messaggio messaggio1 = messaggio.get(0);
        try {
            MessageImage messageImage = messaggio1.getMessageImages().stream().filter(m -> m.getId().equals(id)).findFirst().get();
            messageImage.setActive(false);
            messageImage.setDeletedAt(LocalDate.now().toString());
            messageImage.setModifiedAt(LocalDate.now().toString());
            messageImageRepository.save(messageImage);
            return true;
        } catch (Exception e) {
            throw new BadRequestException("Immagine non trovata");
        }
    }
}
