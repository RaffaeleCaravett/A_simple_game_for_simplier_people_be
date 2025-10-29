package com.example.game.invito;

import com.example.game.enums.InviteState;
import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.InvitoDTO;
import com.example.game.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invito")
@RequiredArgsConstructor
public class InvitoController {
    private final InvitoService invitoService;

    @PostMapping("")
    public Invito crea(@RequestBody @Valid InvitoDTO invitoDTO, BindingResult bindingResult, @AuthenticationPrincipal User user) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return invitoService.save(invitoDTO, user);
    }

    @GetMapping("")
    public List<Invito> getAll(@AuthenticationPrincipal User user) {
        return invitoService.findAll(user);
    }

    @GetMapping("/accetta")
    public Invito accetta(@RequestParam(name = "invito") Long invito, @AuthenticationPrincipal User user) {
        return invitoService.accetta(invito, user);
    }

    @GetMapping("/rifiuta")
    public Invito rifiuta(@RequestParam(name = "invito") Long invito, @AuthenticationPrincipal User user) {
        return invitoService.rifiuta(invito, user);
    }

    @GetMapping("/elimina")
    public Invito elimina(@RequestParam(name = "invito") Long invito, @AuthenticationPrincipal User user) {
        return invitoService.elimina(invito, user);
    }

    @GetMapping("/params")
    public Page<Invito> findAllByParams(@RequestParam Long gioco,
                                        @RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return invitoService.findByParams(null, InviteState.valueOf("SENT"), gioco, null, pageable);
    }
}
