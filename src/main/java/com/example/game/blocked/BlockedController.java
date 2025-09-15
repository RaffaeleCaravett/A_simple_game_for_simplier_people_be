package com.example.game.blocked;

import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.BlockedDTO;
import com.example.game.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blocked")
public class BlockedController {
    @Autowired
    BlockedService blockedService;

    @PostMapping("")
    public Blocked save(@RequestBody @Valid BlockedDTO blockedDTO, BindingResult bindingResult, @AuthenticationPrincipal User user) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return blockedService.save(blockedDTO, user);
    }
    @GetMapping("/unblock/{id}")
    public User unblock(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return blockedService.unblock(id,user);
    }
}
