package com.example.game.connectionRequest;

import com.example.game.enums.EsitoRichiesta;
import com.example.game.payloads.entities.ConnectionRequestDTO;
import com.example.game.user.User;
import com.example.game.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionRequestService {
    private final ConnectionRequestRepository connectionRequestRepository;
    private final UserService userService;

    public ConnectionRequest save(ConnectionRequestDTO connectionRequestDTO, User user) {
        return connectionRequestRepository.save(ConnectionRequest.builder().esitoRichiesta(EsitoRichiesta.INVIATA)
                .sender(user).receiver(userService.findById(connectionRequestDTO.receiverId())).createdAt(LocalDate.now().toString())
                .createdAtDate(LocalDate.now()).build());
    }

    public Page<ConnectionRequest> findByParams(Long senderId, Long receiverId, String senderFullname, String receiverFullname,
                                                String esitoRichiesta, Integer page) {
        PageRequest pageable = PageRequest.of(page, 100);
        return connectionRequestRepository.findAll(Specification.where(
                        ConnectionRequestRepository.senderIdEquals(senderId))
                .and(ConnectionRequestRepository.receiverIdEquals(receiverId))
                .and(ConnectionRequestRepository.receiverFullnameLike(receiverFullname))
                .and(ConnectionRequestRepository.senderFullnameLike(senderFullname))
                .and(ConnectionRequestRepository.stateEquals(EsitoRichiesta.valueOf(esitoRichiesta))), pageable);
    }
}

