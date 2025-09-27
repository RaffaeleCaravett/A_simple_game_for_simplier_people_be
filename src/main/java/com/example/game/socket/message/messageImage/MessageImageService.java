package com.example.game.socket.message.messageImage;


import com.example.game.exceptions.BadRequestException;
import com.example.game.exceptions.NotFoundException;
import com.example.game.exceptions.UnauthorizedException;
import com.example.game.payloads.entities.IdsDTO;
import com.example.game.socket.message.MessageService;
import com.example.game.socket.message.Messaggio;
import com.example.game.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Validated
@RequiredArgsConstructor
public class MessageImageService {

    private final MessageService messageService;
    private final MessageImageRepository messageImageRepository;

    public List<MessageImage> save(List<MultipartFile> multipartFile, Long messaggioId, User user) throws IOException {
        Messaggio messaggio = messageService.findById(messaggioId);
        if (messaggio.getSender().getId() == user.getId()) {
            List<MessageImage> messageImages = new ArrayList<>();
            for (MultipartFile m : multipartFile) {
                MessageImage messageImage = MessageImage.builder()
                        .image(m.getBytes())
                        .name(m.getOriginalFilename())
                        .messaggio(messaggio)
                        .createdAt(LocalDate.now().toString())
                        .createdAtDate(LocalDate.now())
                        .isActive(true).build();
                messageImages.add(messageImageRepository.save(messageImage));
            }
            return messageImages;
        } else {
            throw new UnauthorizedException("Non hai i permessi per salvare questa immagine");
        }
    }

    public List<MessageImage> deleteImageMessage(Long messageId, IdsDTO ids, User user) {
        Messaggio messaggio = messageService.findById(messageId);
        if (messaggio.getSender().getId() != user.getId()) {
            throw new NotFoundException("Messaggio non trovato");
        }
        try {
            List<MessageImage> messageImages = messaggio.getMessageImages();
            List<MessageImage> messageImagesToDelete = new ArrayList<>();

            for (Long l : ids.ids()) {
                messageImagesToDelete.add(findById(l));
            }
            boolean contains = false;
            for (MessageImage messageImage : messageImages) {
                contains = false;
                for (MessageImage messageImage1 : messageImagesToDelete) {
                    if (messageImage.getId().equals(messageImage1.getId())) {
                        contains = true;
                        break;
                    }
                }
                if (contains) {
                    messageImage.setActive(false);
                    messageImage.setDeletedAt(LocalDate.now().toString());
                    messageImage.setModifiedAt(LocalDate.now().toString());
                    messageImageRepository.save(messageImage);
                }
            }
            var messageImagesList = messaggio.getMessageImages();
            if ((null == messageImagesList || messageImagesList.isEmpty())&& (messaggio.getText().isEmpty() )) {
                messageService.delete(messaggio.getId(), user);
                return new ArrayList<>();
            }
            return messaggio.getMessageImages();
        } catch (Exception e) {
            throw new BadRequestException("Immagine non trovata");
        }
    }

    public MessageImage findById(Long id) {
        return messageImageRepository.findById(id).orElseThrow(() -> new NotFoundException("Immagine non trovata"));
    }
}
