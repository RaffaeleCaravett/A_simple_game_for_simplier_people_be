package com.example.game.socket.message.messageImage;

import com.example.game.payloads.entities.IdsDTO;
import com.example.game.user.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/messageImage")
@RequiredArgsConstructor
public class MessageImageController {

    private final MessageImageService messageImageService;

    @PostMapping("/{id}")
    public List<MessageImage> save(@RequestPart(name = "images") List<MultipartFile> multipartFile, @PathVariable Long id, @AuthenticationPrincipal User user) throws IOException {
        return messageImageService.save(multipartFile, id, user);
    }

    @PutMapping("/{id}")
    public List<MessageImage> delete(@PathVariable Long id, @RequestBody @Valid IdsDTO idsDTO, BindingResult bindingResult, @AuthenticationPrincipal User user) {
        return messageImageService.deleteImageMessage(id,idsDTO, user);
    }
}
