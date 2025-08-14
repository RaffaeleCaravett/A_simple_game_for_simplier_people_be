package com.example.game.invito;

import com.example.game.enums.InviteState;
import com.example.game.exceptions.NotFoundException;
import com.example.game.gioco.GiocoService;
import com.example.game.payloads.entities.InvitoDTO;
import com.example.game.user.User;
import com.example.game.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvitoService {
    private final InvitoRepository invitoRepository;
    private final UserService userService;
    private final GiocoService giocoService;


    public Invito save(InvitoDTO invitoDTO, User user){
    return new Invito();
    }
    public List<Invito> findAll(User user){
        return new ArrayList<>();
    }
    public Invito accetta(Long invitoId, User user){
        Invito invito = findById(invitoId);
        if(invito.getReceiver().getId() == user.getId()){
            invito.setInviteState(InviteState.ACCEPTED);
        }
        return invitoRepository.save(invito);
    }
    public Invito rifiuta(Long invitoId, User user){
        Invito invito = findById(invitoId);
        if(invito.getReceiver().getId() == user.getId()){
            invito.setInviteState(InviteState.REFUSED);
        }
        return invitoRepository.save(invito);
    }
    public Invito elimina(Long invitoId, User user){
        Invito invito = findById(invitoId);
        if(invito.getSender().getId() == user.getId()){
            invito.setInviteState(InviteState.CANCELED);
        }
        return invitoRepository.save(invito);
    }
    public Invito findById(Long id){
        return invitoRepository.findById(id).orElseThrow(()-> new NotFoundException("Invito non trovato"));
    }

    public boolean findByParams(Long userId, InviteState inviteState,Long giocoId, Instant instant){
        return invitoRepository.findOne(Specification.where(InvitoRepository.stateEquals(inviteState))
                .and(InvitoRepository.isValid(instant))
                .and(InvitoRepository.giocoIdEquals(giocoId))
                .and(InvitoRepository.receiverIdEquals(userId).or(InvitoRepository.senderIdEquals(userId)))).isPresent();
    }
}
