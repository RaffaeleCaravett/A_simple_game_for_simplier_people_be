package com.example.game.partitaDouble;

import com.example.game.exceptions.BadRequestException;
import com.example.game.exceptions.NotFoundException;
import com.example.game.gioco.GiocoService;
import com.example.game.invito.InvitoService;
import com.example.game.payloads.entities.PartitaDoubleDTO;
import com.example.game.punteggio.Punteggio;
import com.example.game.punteggio.PunteggioService;
import com.example.game.tournament.TournamentService;
import com.example.game.user.User;
import com.example.game.user.UserService;
import jakarta.annotation.Nullable;
import jakarta.mail.Part;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartitaDoubleService {
    private final PartitaDoubleRepository partitaDoubleRepository;
    private final UserService userService;
    private final GiocoService giocoService;
    private final PunteggioService punteggioService;
    private final TournamentService tournamentService;
    private final InvitoService invitoService;

    public PartitaDouble create(PartitaDoubleDTO partitaDoubleDTO) {
        return partitaDoubleRepository.save(PartitaDouble.builder().partecipanti(partitaDoubleDTO.partecipanti().stream().map(userService::findById).collect(Collectors.toSet()))
                .invito(invitoService.findById(partitaDoubleDTO.invito())).createdAt(LocalDate.now().toString())
                .createdAtDate(LocalDate.now()).tournament(null != partitaDoubleDTO.torneo() ? tournamentService.findById(partitaDoubleDTO.torneo()) : null)
                .isActive(true).gioco(giocoService.findById(partitaDoubleDTO.gioco())).build());
    }

    public Page<PartitaDouble> get(Integer page, Integer size, @Nullable List<Long> partecipanti, List<Long> vincenti, Long torneo, Long gioco) {
        Pageable pageable = PageRequest.of(page, size);
        return partitaDoubleRepository.findAll(Specification.where(PartitaDoubleRepository.partecipantiIn(partecipanti)).and(PartitaDoubleRepository.vincitoriIn(vincenti))
                .and(PartitaDoubleRepository.giocoId(gioco)).and(PartitaDoubleRepository.torneoId(torneo)), pageable);
    }

    public PartitaDouble win(Long id, List<Long> vincenti, String punteggioPerdenti, String punteggioVincenti) {
        PartitaDouble partitaDouble = findById(id);
        if (vincenti.isEmpty()) {
            throw new BadRequestException("Vincenti necessari");
        }
        vincenti.forEach(v -> {
            if (!partitaDouble.getPartecipanti().stream().map(User::getId).toList().contains(v)) {
                throw new BadRequestException("Non puoi vincere una persona che non ha partecipato");
            }
        });

        partitaDouble.setVincitori(vincenti.stream().map(userService::findById).collect(Collectors.toSet()));
        partitaDoubleRepository.save(partitaDouble);
        Punteggio punteggio = Punteggio.builder().punteggio(punteggioVincenti).partitaDouble(partitaDouble).createdAt(LocalDate.now().toString())
                .createdAtDate(LocalDate.now()).build();
        punteggioService.save(punteggio);
        partitaDouble.setPunteggioPerdenti(punteggioPerdenti);
        partitaDouble.setPunteggioVincenti(punteggio);
        return partitaDoubleRepository.save(partitaDouble);

    }

    public PartitaDouble findById(Long id) {
        return partitaDoubleRepository.findById(id).orElseThrow(() -> new NotFoundException("Partita non trovata"));
    }

    public PartitaDouble save(PartitaDouble partitaDouble) {
        return partitaDoubleRepository.save(partitaDouble);
    }
}
