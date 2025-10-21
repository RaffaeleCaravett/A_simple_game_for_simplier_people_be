package com.example.game.scheduled.tournament;

import com.example.game.enums.NotificationState;
import com.example.game.enums.NotificationType;
import com.example.game.enums.TournamentState;
import com.example.game.notification.Notification;
import com.example.game.notification.NotificationRepository;
import com.example.game.notification.NotificationService;
import com.example.game.payloads.entities.NotificationDTO;
import com.example.game.payloads.entities.SocketDTO;
import com.example.game.socket.connection.ConnectionController;
import com.example.game.tournament.Tournament;
import com.example.game.tournament.TournamentRepository;
import com.example.game.tournament.TournamentService;
import com.example.game.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
@Component
public class ScheduledTournamentService {

    private final TournamentService tournamentService;
    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final ConnectionController connectionController;
    @Transactional
    @Scheduled(cron = "1 0 0 * * ?")
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
                t.setTournamentState(TournamentState.ANNUNCIATO);
                tournamentService.save(t);
                log.info("Changed announced : {}", t.getName());
            }
            if (t.getStartDate().isBefore(todayDate)) {
                t.setTournamentState(TournamentState.IN_CORSO);
                tournamentService.save(t);
                log.info("Changed announced : {}", t.getName());
            }
            if (t.getEndDate().isBefore(todayDate)) {
                t.setTournamentState(TournamentState.TERMINATO);
                tournamentService.save(t);
            }
            if(t.getStartDate().equals(todayDate)){
                t.setTournamentState(TournamentState.IN_CORSO);
                tournamentService.save(t);
                createNotification(t);
            }
        }
        for (Tournament t : runningTournaments.getContent()) {
            if(t.getStartDate().isBefore(todayDate)){
                t.setTournamentState(TournamentState.IN_CORSO);
                createNotification(t);
                tournamentService.save(t);
            }
            if (t.getEndDate().isBefore(todayDate)) {
                t.setTournamentState(TournamentState.TERMINATO);
                tournamentService.save(t);
                log.info("Changed running : {}", t.getName());
            }
            if (t.getStartDate().isAfter(todayDate)) {
                t.setTournamentState(TournamentState.ANNUNCIATO);
                tournamentService.save(t);
                log.info("Changed running : {}", t.getName());
            }
            if(t.getStartDate().equals(todayDate)){
                t.setTournamentState(TournamentState.IN_CORSO);
                tournamentService.save(t);
                createNotification(t);
            }
        }
        log.info("Method finished.");
    }


    public void createNotification(Tournament t){
        userService.findAll(true).forEach(u -> {
            Notification notification = this.notificationRepository.save(Notification.builder()
                    .testo("Oggi è iniziato un nuovo torneo : " + t.getName() + "!")
                    .notificationType(NotificationType.TOURNAMENT)
                    .createdAtDate(LocalDate.now()).isActive(true)
                    .receiver(u)
                    .createdAt(LocalDate.now().toString())
                    .sender(null)
                    .state(NotificationState.SENT)
                    .chat(null).build());
            log.info("Created notification. " + notification.getTesto());
            tournamentService.save(t);
            try {
                connectionController.addMessage(SocketDTO.builder()
                        .connectionDTO(null).messageDTO(null).gameConnectionDTO(null).connectionRequestDTO(null).notification(notification).build());
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
