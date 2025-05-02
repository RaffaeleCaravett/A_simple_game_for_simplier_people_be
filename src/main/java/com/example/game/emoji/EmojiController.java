package com.example.game.emoji;

import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.EmojiDTO;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emojies")
@RequiredArgsConstructor
@Validated
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
    @PostMapping("")
    public List<Emoji> save(@RequestBody @Validated List<@Valid EmojiDTO> emojiDTOS, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return emojiService.save(emojiDTOS);
    }
}
