package com.example.game.emoji;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/emojies")
@RequiredArgsConstructor
public class EmojiController {

    private final EmojiService emojiService;


    @GetMapping("")
    public List<Emoji> findAll(){
        return emojiService.findAll();
    }
    @GetMapping("/{title}")
    public List<Emoji> findAllByTitle(@PathVariable String title){
        return emojiService.findAllByTitle(title);
    }
}
