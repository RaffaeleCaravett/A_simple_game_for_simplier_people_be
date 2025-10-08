package com.example.game.tournament;

import com.example.game.enums.TournamentState;
import com.example.game.exceptions.BadRequestException;
import com.example.game.gioco.Gioco;
import com.example.game.gioco.GiocoService;
import com.example.game.payloads.entities.TournamentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final GiocoService giocoService;

    public List<Tournament> findAll() {
        return tournamentRepository.findAll();
    }

    public Tournament create(TournamentDTO tournamentDTO) {
        var tornei = findIfExist(tournamentDTO.nome());
        if (!tornei.isEmpty()) {
            throw new BadRequestException("Torneo già registrato.");
        }
        Gioco gioco = giocoService.findById(tournamentDTO.gioco_id());
        return tournamentRepository.save(Tournament.builder().tournamentState(TournamentState.valueOf(tournamentDTO.stato())).name(tournamentDTO.nome())
                .createdAtDate(LocalDate.now()).isActive(true).gioco(gioco).endDate(tournamentDTO.dateTo()).createdAt(LocalDate.now().toString())
                .startDate(tournamentDTO.dateFrom()).build());
    }

    public Page<Tournament> getAll(Pageable pageable, String nome, String gioco, LocalDate creazione,
                                   LocalDate inizio, LocalDate fine, String stato, Boolean isActive) {

        return tournamentRepository.findAll(Specification.where(TournamentRepository.dataCreazioneEquals(creazione))
                .and(TournamentRepository.dataFineEquals(fine))
                .and(TournamentRepository.dataInizioEquals(inizio))
                .and(TournamentRepository.statoEquals(stato != null ? TournamentState.valueOf(stato) : null))
                .and(TournamentRepository.nomeLike(nome))
                .and(TournamentRepository.giocoLike(gioco))
                .and(TournamentRepository.isActive(isActive)), pageable);
    }

    public List<Tournament> getByGiocoId(Long giocoId) {
        return new ArrayList<>();
    }

    public boolean deleteById(Long id) {
        try {
            tournamentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new BadRequestException("Ci sono problemi ad eliminare il torneo, contatta l'amministrazione");
        }
    }

    public List<Tournament> findIfExist(String nome) {
        return tournamentRepository.findAll(Specification.where(TournamentRepository.nomeEquals(nome))
                .and(TournamentRepository.statoNotEquals(TournamentState.TERMINATO)));
    }

    public Tournament put(Long id, Object o, String nome) {
        Tournament tournament = findById(id);
        List<Tournament> tournament1 = findIfExist(nome);
        if (!tournament1.isEmpty() && !tournament1.get(0).getId().equals(tournament.getId())) {
            throw new BadRequestException("Torneo con questo nome già presente");
        }
        tournament.setName(nome);
        return tournamentRepository.save(tournament);
    }

    public Tournament findById(Long id) {
        return tournamentRepository.findById(id).orElseThrow(() -> new BadRequestException("Torneo non trovato"));
    }

    public Tournament save(Tournament t) {
        return tournamentRepository.save(t);
    }
}
