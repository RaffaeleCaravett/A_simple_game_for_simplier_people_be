package com.example.game.emoji;

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
}
