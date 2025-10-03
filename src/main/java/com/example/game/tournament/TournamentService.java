package com.example.game.tournament;

import com.example.game.enums.TournamentState;
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

    public Tournament create(TournamentDTO tournamentDTO) {
        Gioco gioco = giocoService.findById(tournamentDTO.gioco_id());
        return tournamentRepository.save(Tournament.builder().tournamentState(TournamentState.valueOf(tournamentDTO.stato())).name(tournamentDTO.nome())
                .createdAtDate(LocalDate.now()).endDate(tournamentDTO.dateTo()).createdAt(LocalDate.now().toString())
                .startDate(tournamentDTO.dateFrom()).build());
    }

    public Page<Tournament> getAll(Pageable pageable, String nome, String gioco, LocalDate creazione,
                                   LocalDate inizio, LocalDate fine, String stato) {

        return tournamentRepository.findAll(Specification.where(TournamentRepository.dataCreazioneEquals(creazione))
                .and(TournamentRepository.dataFineEquals(fine))
                .and(TournamentRepository.dataInizioEquals(inizio))
                .and(TournamentRepository.statoEquals(stato != null ? TournamentState.valueOf(stato) : null))
                .and(TournamentRepository.nomeLike(nome))
                .and(TournamentRepository.giocoLike(gioco)), pageable);
    }

    public List<Tournament> getByGiocoId(Long giocoId) {
        return new ArrayList<>();
    }

    public boolean deleteById(Long id) {
        return true;
    }

    public Tournament put(Long id, TournamentDTO tournamentDTO) {
        return new Tournament();
    }
}
