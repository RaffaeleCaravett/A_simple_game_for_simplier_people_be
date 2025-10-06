package com.example.game.scheduled.tournament;

import com.example.game.enums.TournamentState;
import com.example.game.tournament.Tournament;
import com.example.game.tournament.TournamentRepository;
import com.example.game.tournament.TournamentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledTournamentService {

    private final TournamentService tournamentService;

    @Scheduled(cron = "0 1 24 * * ?")
    public void CheckTournamentStatus() {
        log.info("Starting to retrieve tournaments");
        Page<Tournament> announcedTournaments = tournamentService.getAll(PageRequest.of(0, 10000), null, null, null, null, null,
                "ANNUNCIATO", true);
        Page<Tournament> runningTournaments = tournamentService.getAll(PageRequest.of(0, 10000), null, null, null, null, null,
                "IN_CORSO", true);
        log.info("Tournaments retrieved");

        var todayDate = LocalDate.now();

        for (Tournament t : announcedTournaments.getContent()) {
            if (t.getStartDate().isAfter(todayDate)) {
                t.setTournamentState(TournamentState.IN_CORSO);
                tournamentService.save(t);
                log.info("Changed announced : {}", t.getName());
            }
        }
        for (Tournament t : runningTournaments.getContent()) {
            if (t.getEndDate().isAfter(todayDate)) {
                t.setTournamentState(TournamentState.TERMINATO);
                tournamentService.save(t);
                log.info("Changed running : {}", t.getName());
            }
        }
        log.info("Method finished.");
    }
}
