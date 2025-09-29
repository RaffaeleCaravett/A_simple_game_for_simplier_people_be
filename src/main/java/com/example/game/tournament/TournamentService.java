package com.example.game.tournament;

import com.example.game.payloads.entities.TournamentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;


    public Tournament create(TournamentDTO tournamentDTO){
        return new Tournament();
    }
    public Page<Tournament> getAll(Pageable pageable){
        return null;
    }
    public List<Tournament> getByGiocoId(Long giocoId){
     return new ArrayList<>();
    }
    public boolean deleteById(Long id){
        return true;
    }
    public Tournament put(Long id, TournamentDTO tournamentDTO){
        return new Tournament();
    }
}
