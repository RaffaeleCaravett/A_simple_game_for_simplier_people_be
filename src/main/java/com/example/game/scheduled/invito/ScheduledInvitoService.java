package com.example.game.scheduled.invito;

import com.example.game.enums.InviteState;
import com.example.game.invito.Invito;
import com.example.game.invito.InvitoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Component
public class ScheduledInvitoService {

    private final InvitoService invitoService;

    @Scheduled(cron = "0 0/25 * * * *")
    private void deletePendingInvites() {
        log.info("Starting to retrieve invites ....");

        List<Invito> invitos = invitoService.findAllByParams(null, InviteState.SENT, null, null);

        if (invitos.isEmpty()) return;
        invitos.forEach(inv -> {
            if (ChronoUnit.MINUTES.between(inv.getCreatedAt(), Instant.now()) > 5) {
                inv.setInviteState(InviteState.EXPIRED);
                invitoService.update(inv);
                log.info("Expired " + inv.getId());
            }
        });

        log.info("Job finished");
    }
}
