package com.example.game.tournament;

import com.example.game.emoji.Emoji;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TournamentRepository extends JpaRepository<Tournament,Long>, JpaSpecificationExecutor<Tournament> {
}
