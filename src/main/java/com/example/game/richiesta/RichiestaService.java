package com.example.game.richiesta;

import com.example.game.exceptions.NotFoundException;
import com.example.game.exceptions.UnauthorizedException;
import com.example.game.payloads.entities.RichiestaDTO;
import com.example.game.user.User;
import com.example.game.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RichiestaService {
    private final RichiestaRepository richiestaRepository;
    private final UserService userService;

    public Richiesta save(RichiestaDTO richiestaDTO) {

        return richiestaRepository.save(Richiesta.builder()
                .descrizione(richiestaDTO.descrizione())
                .descrizionePreview(formatDescrizione(richiestaDTO.descrizione()))
                .oggetto(richiestaDTO.oggetto())
                .user(userService.findById(richiestaDTO.userId()))
                .createdAt(LocalDate.now().toString())
                .createdAtDate(LocalDate.now())
                .modifiedAt(LocalDate.now().toString())
                .isActive(true)
                .build());
    }

    public Page<Richiesta> getByUserId(Long userId, Pageable pageable) {
        return richiestaRepository.findByUser_IdAndIsActive(userId,true, pageable);
    }

    public Richiesta modifyRichiesta(Long userId, RichiestaDTO richiestaDTO, Long richiestaId) {
        if (!richiestaDTO.userId().equals(userId)) {
            throw new UnauthorizedException("Non sei autorizzato a modificare questa richiesta");
        }
        Richiesta richiesta = findById(richiestaId);
        richiesta.setOggetto(richiestaDTO.oggetto());
        richiesta.setDescrizione(richiestaDTO.descrizione());
        richiesta.setDescrizionePreview(formatDescrizione(richiestaDTO.descrizione()));
        richiesta.setModifiedAt(LocalDate.now().toString());
        return richiestaRepository.save(richiesta);
    }

    public Richiesta findById(Long richiestaId) {
        return richiestaRepository.findById(richiestaId).orElseThrow(() -> new NotFoundException("Richiesta con id " + richiestaId + " non trovata in db."));
    }

    public Page<Richiesta> byFilters(String descrizione, String oggetto, Long userId, LocalDate from, LocalDate to, Pageable pageable) {
        return richiestaRepository.findAll(Specification.where(RichiestaRepository.oggettoLike(oggetto))
                .and(RichiestaRepository.descrizioneLike(descrizione))
                .and(RichiestaRepository.userIdEquals(userId))
                .and(RichiestaRepository.createdAtBetween(from, to)), pageable);
    }

    public String formatDescrizione(String descrizione) {
        if (descrizione.length() >= 90) return descrizione.substring(0, 90) + " ...";
        return descrizione + " ...";
    }
    public  Boolean delete(User user, Long richiestaId){
        try {
            Richiesta richiesta = findById(richiestaId);
            if(richiesta.getUser().getId()!=user.getId()) throw new UnauthorizedException("Non puoi cancellare questa richiesta");
            richiesta.setDeletedAt(LocalDate.now().toString());
            richiesta.setActive(false);
            richiestaRepository.save(richiesta);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
