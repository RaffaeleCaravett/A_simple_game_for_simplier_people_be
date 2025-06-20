package com.example.game.recensione;

import com.example.game.exceptions.RecensioneNotFoundException;
import com.example.game.exceptions.UnauthorizedException;
import com.example.game.exceptions.ValidationAlreadyExistsException;
import com.example.game.gioco.GiocoRepository;
import com.example.game.gioco.GiocoService;
import com.example.game.payloads.entities.RecensioneDTO;
import com.example.game.payloads.entities.RecensioneGiocoDTO;
import com.example.game.user.User;
import com.example.game.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.*;

@Service
@Validated
@RequiredArgsConstructor
public class RecensioneService {

    private final RecensioneRepository recensioneRepository;
    private final GiocoService giocoService;

    public Recensione save(User user, RecensioneDTO recensioneDTO) {

        canPostValidation(user.getId(), recensioneDTO.giocoId());

        Recensione recensione = new Recensione();
        recensione.setCommento(recensioneDTO.commento());
        if(!recensione.getCommento().isBlank() && !recensione.getCommento().isEmpty()&&recensione.getCommento().length()>30){
            recensione.setCommentoPreview(recensione.getCommento().substring(0,29)+" ...");
        }else{
            recensione.setCommentoPreview(recensione.getCommento());
        }
        recensione.setUser(user);
        recensione.setPunteggio(recensioneDTO.punteggio());
        recensione.setGioco(giocoService.findById(recensioneDTO.giocoId()));
        recensione.setActive(true);
        recensione.setCreatedAt(LocalDate.now().toString());
        recensione.setModifiedAt(LocalDate.now().toString());
        recensione.setCreatedAtDate(LocalDate.now());
        recensione.getGioco().setTotalRecensioniNumber(recensione.getGioco().getTotalRecensioniNumber() + 1);

        return recensioneRepository.save(recensione);
    }

    public void canPostValidation(Long userId, Long giocoId) {
        if (recensioneRepository.findByUser_IdAndGioco_IdAndIsActive(userId, giocoId, true).isPresent()) {
            throw new ValidationAlreadyExistsException("Hai già recensito questo gioco!");
        }
    }

    public Page<RecensioneGiocoDTO> findAllByUserId(long userId, int page, int size, String orderBy, String sordOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sordOrder), orderBy));
        var recensioni = recensioneRepository.findAllByUser_IdAndIsActive(userId, true, pageable);
        List<RecensioneGiocoDTO> recensioneGiocoDTOS = recensioni.getContent().stream().map(
                recensione -> new RecensioneGiocoDTO(recensione.getId(), recensione.getPunteggio(), recensione.getGioco().getNomeGioco(), recensione.getGioco().getImage(), recensione.getCommento(), recensione.getCreatedAt(), recensione.getUser().getFullName(), recensione.getUser().getImmagineProfilo())
        ).toList();
        return new PageImpl<RecensioneGiocoDTO>(
                recensioneGiocoDTOS,
                recensioni.getPageable(),
                recensioni.getTotalElements()
        );
    }

    public Page<Recensione> findAllByUserIdAndPunteggio(long userId, int punteggio, int page, int size, String orderBy, String sordOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sordOrder), orderBy));
        return recensioneRepository.findAllByUser_IdAndPunteggioAndIsActive(userId, punteggio, true, pageable);
    }

    public Page<Recensione> findAllByGiocoIdAndPunteggio(long giocoId, int punteggio, int page, int size, String orderBy, String sordOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sordOrder), orderBy));
        return recensioneRepository.findAllByGioco_IdAndPunteggioAndIsActive(giocoId, punteggio, true, pageable);
    }

    public boolean deleteRecensione(long userId, long recensioneId) {
        Recensione recensione = findById(recensioneId);
        if (recensione.getUser().getId() != userId) {
            throw new UnauthorizedException("Non sei autorizzato a cancellare le recensioni degli altri.");
        }
        recensione.setActive(false);
        recensione.getGioco().setTotalRecensioniNumber(recensione.getGioco().getTotalRecensioniNumber() - 1);
        recensioneRepository.save(recensione);
        return true;
    }

    public Recensione findById(long recensioneId) {
        return recensioneRepository.findByIdAndIsActive(recensioneId, true).orElseThrow(() -> new RecensioneNotFoundException(recensioneId));
    }

    public Recensione putById(long userId, long recensioneId, RecensioneDTO recensioneDTO) {
        Recensione recensione = findById(recensioneId);
        if (recensione.getUser().getId() != userId) {
            throw new UnauthorizedException("Non sei autorizzato a cancellare le recensioni degli altri.");
        }
        ;
        recensione.setPunteggio(recensioneDTO.punteggio());
        recensione.setCommento(recensioneDTO.commento());
        if(!recensione.getCommento().isBlank() && !recensione.getCommento().isEmpty()&&recensione.getCommento().length()>30){
            recensione.setCommentoPreview(recensione.getCommento().substring(0,29)+" ...");
        }else{
            recensione.setCommentoPreview(recensione.getCommento());
        }
        return recensioneRepository.save(recensione);
    }

    public Recensione findByUserIdAndGiocoId(long userId, long giocoId) {
        Optional<Recensione> recensione = recensioneRepository.findByUser_IdAndGioco_IdAndIsActive(userId, giocoId, true);

        return recensione.orElse(null);
    }

    public Page<Recensione> findAllByGiocoId(long id, long userId, int page, int size, String orderBy, String sortOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), orderBy));
        return recensioneRepository.findAllByGioco_IdAndIsActiveAndUser_IdNot(id, true, userId, pageable);
    }
}
