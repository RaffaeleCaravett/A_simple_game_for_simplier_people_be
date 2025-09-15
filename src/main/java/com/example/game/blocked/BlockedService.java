package com.example.game.blocked;

import com.example.game.exceptions.BadRequestException;
import com.example.game.exceptions.UnauthorizedException;
import com.example.game.payloads.entities.BlockedDTO;
import com.example.game.user.User;
import com.example.game.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Valid
@RequiredArgsConstructor
public class BlockedService {
    private final BlockedRepository blockedRepository;
    private final UserService userService;

    public Blocked save(BlockedDTO blockedDTO, User user) {
        List<Blocked> optionalBlockeds = blockedRepository.findAll(Specification.where(
                BlockedRepository.blockedIdEquals(blockedDTO.utente_id())
                        .and(BlockedRepository.blockerIdEquals(user.getId()))
        ));
        if (!optionalBlockeds.isEmpty()) {
            throw new BadRequestException("Hai già bloccato quest persona");
        }
        Blocked blocked = Blocked.builder().blocked(userService.findById(blockedDTO.utente_id())).blocker(user)
                .createdAt(LocalDate.now().toString()).createdAtDate(LocalDate.now()).modifiedAt(LocalDate.now().toString()).isActive(true).build();
        return blockedRepository.save(blocked);
    }

    public User unblock(Long id, User user) {
        Blocked blocked = findById(id);
        if (blocked.getBlocker().getId() != user.getId()) {
            throw new UnauthorizedException("Non puoi sbloccare qualcuno che non hai bloccato tu");
        }
        blocked.setActive(false);
        blocked.setDeletedAt(LocalDate.now().toString());
        blocked.setModifiedAt(LocalDate.now().toString());
        blockedRepository.save(blocked);
        return blocked.getBlocked();
    }

    public Blocked findById(Long blockedId) {
        return blockedRepository.findById(blockedId).orElseThrow(() -> new BadRequestException("Impossibile trovare l'utente bloccato"));
    }
}
