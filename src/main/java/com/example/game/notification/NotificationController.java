package com.example.game.notification;

import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.NotificationDTO;
import com.example.game.payloads.entities.NotificationsIdsDTO;
import com.example.game.user.User;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @PostMapping("")
    public Notification save(@RequestBody @Valid NotificationDTO notificationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return notificationService.save(notificationDTO);
    }

    @PostMapping("/read")
    public boolean readAll(@RequestBody @Valid NotificationsIdsDTO notificationIdsDTO, BindingResult bindingResult, @AuthenticationPrincipal User user) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return notificationService.read(notificationIdsDTO, user);
    }

    @GetMapping("")
    public List<Notification> getAll(@AuthenticationPrincipal User user) {
        return notificationService.getAllByUserId(user.getId());
    }
}
