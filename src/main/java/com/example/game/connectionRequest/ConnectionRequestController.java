package com.example.game.connectionRequest;

import com.example.game.exceptions.BadRequestException;
import com.example.game.notification.Notification;
import com.example.game.payloads.entities.ConnectionRequestDTO;
import com.example.game.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/connectionRequest")
@RequiredArgsConstructor
public class ConnectionRequestController {

    private final ConnectionRequestService connectionRequestService;

    @PostMapping("")
    @Transactional
    public ConnectionRequest save(@RequestBody @Valid ConnectionRequestDTO connectionRequestDTO, BindingResult bindingResult,
                                  @AuthenticationPrincipal User user) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return connectionRequestService.save(connectionRequestDTO, user);
    }

    @GetMapping("")
    Page<ConnectionRequest> getAll(@RequestParam(required = false) Long senderId,
                                   @RequestParam(required = false) Long receiverId,
                                   @RequestParam(required = false) String senderFullname,
                                   @RequestParam(required = false) String receiverFullname,
                                   @RequestParam(required = false) String esitoRichiesta,
                                   @RequestParam(defaultValue = "0") Integer page) {
        return connectionRequestService.findByParams(senderId, receiverId, senderFullname, receiverFullname, esitoRichiesta, page);
    }

    @GetMapping("/friend")
    Boolean isFriend(@RequestParam(required = false) Long userId1,
                     @RequestParam(required = false) Long userId2) {
        return connectionRequestService.checkIfFriends(userId1, userId2);
    }

    @GetMapping("/accept")
    ConnectionRequest accept(@RequestParam Long requestId, @AuthenticationPrincipal User user) {
        return connectionRequestService.accept(requestId, user);
    }

    @GetMapping("/refuse")
    ConnectionRequest refuse(@RequestParam Long requestId, @AuthenticationPrincipal User user) {
        return connectionRequestService.refuse(requestId, user);
    }

    @GetMapping("/delete")
    ConnectionRequest delete(@RequestParam Long requestId, @AuthenticationPrincipal User user) {
        return connectionRequestService.delete(requestId, user);
    }

    @GetMapping("/deleteByIds")
    boolean deleteFriend(@AuthenticationPrincipal User user, @RequestParam Long visitedUserId) {
        return connectionRequestService.deleteFriend(visitedUserId, user);
    }
}
