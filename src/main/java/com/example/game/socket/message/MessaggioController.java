package com.example.game.socket.message;

import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.MessageDTO;
import com.example.game.payloads.entities.ReadReceiptRequestDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messaggi")
@RequiredArgsConstructor
public class MessaggioController {

    @Autowired
    MessageRepository messageRepository;
    private final MessageService messageService;

    @GetMapping("/messages/{channelId}")
    public Page<Messaggio> findMessages(Pageable pageable, @PathVariable("channelId") String channelId) {
        return messageRepository.findAll(Specification.where(MessageRepository.chatIdEquals(Long.valueOf(channelId)))
                , pageable);
    }

    @PostMapping("/messages")
    public void sendReadReceipt(@RequestBody @Valid ReadReceiptRequestDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BadRequestException(bindingResult.getAllErrors());
        messageRepository.sendReadReceipt(request.chatId(), request.senderId());
    }

    @PostMapping("")
    @Transactional
    public Messaggio save(@RequestBody @Valid MessageDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BadRequestException(bindingResult.getAllErrors());
        return messageService.save(request);
    }
}
