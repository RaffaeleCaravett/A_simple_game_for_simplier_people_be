package com.example.game.recensione;

import com.example.game.exceptions.ValidationAlreadyExistsException;
import com.example.game.gioco.GiocoRepository;
import com.example.game.gioco.GiocoService;
import com.example.game.payloads.entities.RecensioneDTO;
import com.example.game.user.User;
import com.example.game.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@Validated
@RequiredArgsConstructor
public class RecensioneService {

    private final RecensioneRepository recensioneRepository;
    private final AuthenticationPrincipal authenticationPrincipal;
    private final GiocoService giocoService;
    private final UserService userService;
    public Recensione save(@AuthenticationPrincipal User user, RecensioneDTO recensioneDTO){

        canPostValidation(user.getId(), recensioneDTO.giocoId());

        Recensione recensione = new Recensione();
        recensione.set

    }

    public void canPostValidation(Long userId,Long giocoId){
        if(recensioneRepository.findByUser_idAndGiocoId(userId,giocoId).isPresent()){
            throw new ValidationAlreadyExistsException("Hai già recensito questo gioco!");
        }
    }

}
