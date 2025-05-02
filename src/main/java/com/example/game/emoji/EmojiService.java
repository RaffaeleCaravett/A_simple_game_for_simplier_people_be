package com.example.game.emoji;

import com.example.game.payloads.entities.EmojiDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class EmojiService {

    private final EmojiRepository emojiRepository;


    public List<Emoji> findAll(){
        return emojiRepository.findAll();
    }

    public List<Emoji> findAllByTitle(String title){
        return emojiRepository.findAllByTitle(title);
    }
    public List<Emoji> save(List<EmojiDTO> emojiDTOS){
        return emojiDTOS.stream().map(this::map).map(emojiRepository::save).toList();
    }

    private Emoji map(EmojiDTO emojiDTO){
        return Emoji.builder().
                emoji(emojiDTO.emoji())
                .field(emojiDTO.field())
                .title(emojiDTO.title())
                .build();
    }
}
