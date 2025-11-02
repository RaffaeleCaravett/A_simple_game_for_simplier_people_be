package com.example.game.invito;

import com.example.game.enums.InviteState;
import com.example.game.exceptions.BadRequestException;
import com.example.game.exceptions.NotFoundException;
import com.example.game.gioco.GiocoService;
import com.example.game.payloads.entities.InvitoDTO;
import com.example.game.payloads.entities.InvitoSentDTO;
import com.example.game.user.User;
import com.example.game.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Invito persist(Invito invito) {
        return invitoRepository.save(invito);
    }

    public Invito save(InvitoSentDTO invitoDTO, User user) {
        if (!findByParams(user.getId(), InviteState.SENT, null, null, PageRequest.of(0, 1)).getContent().isEmpty()) {
            throw new BadRequestException("Hai già un invito attivo! Aspetta che scada l'invito per invitare qualcuno!");
        }

        return invitoRepository.save(Invito.builder().createdAt(Instant.now()).inviteState(InviteState.SENT).sender(userService.findById(user.getId())).gioco(giocoService.findById(invitoDTO.giocoId())).build());
    }

    public List<Invito> findAll(User user) {
        return new ArrayList<>();
    }

    public Invito accetta(Long invitoId, User user) {
        Invito invito = findById(invitoId);
        invito.setInviteState(InviteState.ACCEPTED);
        return invitoRepository.save(invito);
    }

    public Invito rifiuta(Long invitoId, User user) {
        Invito invito = findById(invitoId);
        invito.setInviteState(InviteState.REFUSED);
        return invitoRepository.save(invito);
    }

    public Invito elimina(Long invitoId, User user) {
        Invito invito = findById(invitoId);
        if (invito.getSender().getId() == user.getId()) {
            invito.setInviteState(InviteState.CANCELED);
        }
        return invitoRepository.save(invito);
    }

    public Invito findById(Long id) {
        return invitoRepository.findById(id).orElseThrow(() -> new NotFoundException("Invito non trovato"));
    }

    public Page<Invito> findByParams(Long userId, InviteState inviteState, Long giocoId, Instant instant, Pageable pageable) {
        return invitoRepository.findAll(Specification.where(InvitoRepository.stateEquals(inviteState))
                .and(InvitoRepository.isValid(instant))
                .and(InvitoRepository.giocoIdEquals(giocoId))
                .and(InvitoRepository.senderIdEquals(userId)), pageable);
    }

    public List<Invito> findAllByParams(Long userId, InviteState inviteState, Long giocoId, Instant instant) {
        return invitoRepository.findAll(Specification.where(InvitoRepository.stateEquals(inviteState))
                .and(InvitoRepository.isValid(instant))
                .and(InvitoRepository.giocoIdEquals(giocoId))
                .and(InvitoRepository.senderIdEquals(userId)));
    }

    public void update(Invito invito) {
        invitoRepository.save(invito);
    }
}
