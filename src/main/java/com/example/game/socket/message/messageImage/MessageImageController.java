package com.example.game.socket.message.messageImage;

import com.example.game.user.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/messageImage")
@RequiredArgsConstructor
public class MessageImageController {

    private final MessageImageService messageImageService;

    @PostMapping("/{id}")
    public MessageImage save(@RequestPart(name = "image") MultipartFile multipartFile, @PathVariable Long id, @AuthenticationPrincipal User user) throws IOException {
        return messageImageService.save(multipartFile, id, user);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return messageImageService.deleteImageMessage(id, user);
    }
}
