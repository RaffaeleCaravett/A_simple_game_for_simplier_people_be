package com.example.game.connectionRequest;

import com.example.game.enums.EsitoRichiesta;
import com.example.game.enums.NotificationState;
import com.example.game.enums.NotificationType;
import com.example.game.exceptions.BadRequestException;
import com.example.game.exceptions.UnauthorizedException;
import com.example.game.notification.Notification;
import com.example.game.notification.NotificationRepository;
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
    private final NotificationRepository notificationRepository;

    public ConnectionRequest save(ConnectionRequestDTO connectionRequestDTO, User user) {

        var connectionRequest = connectionRequestRepository.findAll(Specification.where(
                        ConnectionRequestRepository.sendeIdrOrReceiverIdEquals(user.getId(), connectionRequestDTO.receiverId()))
                .and(ConnectionRequestRepository.stateEquals(EsitoRichiesta.INVIATA)));

        if (!connectionRequest.isEmpty()) {
            throw new BadRequestException("Hai già una richiesta attiva inviata a questo utente");
        } else {
            connectionRequest = connectionRequestRepository.findAll(Specification.where(ConnectionRequestRepository.senderIdEquals(user.getId()))
                    .and(ConnectionRequestRepository.senderIdEquals(connectionRequestDTO.receiverId()))
                    .and(ConnectionRequestRepository.stateEquals(EsitoRichiesta.INVIATA)));
        }

        if (!connectionRequest.isEmpty()) {
            throw new BadRequestException("Hai già una richiesta attiva inviata a questo utente");
        }
        var c = connectionRequestRepository.save(ConnectionRequest.builder().esitoRichiesta(EsitoRichiesta.INVIATA)
                .sender(user).receiver(userService.findById(connectionRequestDTO.receiverId())).createdAt(LocalDate.now().toString())
                .createdAtDate(LocalDate.now()).build());
        notificationRepository.save(Notification.builder().sender(user).receiver(c.getReceiver())
                .state(NotificationState.SENT).chat(null).notificationType(NotificationType.CONNECTION_REQUEST).createdAt(LocalDate.now().toString())
                .createdAtDate(LocalDate.now()).testo(user.getFullName() + " ti ha mandato una richiesta di contatto!").build());
        return c;
    }

    public Page<ConnectionRequest> findByParams(Long senderId, Long receiverId, String senderFullname, String receiverFullname,
                                                String esitoRichiesta, Integer page) {
        PageRequest pageable = PageRequest.of(page, 100);
        return connectionRequestRepository.findAll(Specification.where(
                        ConnectionRequestRepository.senderIdEquals(senderId))
                .and(ConnectionRequestRepository.receiverIdEquals(receiverId))
                .and(ConnectionRequestRepository.receiverFullnameLike(receiverFullname))
                .and(ConnectionRequestRepository.senderFullnameLike(senderFullname))
                .and(ConnectionRequestRepository.stateEquals(esitoRichiesta != null ? EsitoRichiesta.valueOf(esitoRichiesta.toUpperCase()) : null)), pageable);
    }

    public List<ConnectionRequest> findAllByParams(Long senderId, Long receiverId, String esitoRichiesta) {
        return connectionRequestRepository.findAll(Specification.where(
                        ConnectionRequestRepository.senderIdEquals(senderId))
                .and(ConnectionRequestRepository.receiverIdEquals(receiverId))
                .and(ConnectionRequestRepository.stateEquals(esitoRichiesta != null ? EsitoRichiesta.valueOf(esitoRichiesta.toUpperCase()) : null)));
    }

    public Boolean checkIfFriends(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) {
            return true;
        }
        List<ConnectionRequest> connectionRequests = connectionRequestRepository.findAll(Specification.where(
                ConnectionRequestRepository.sendeIdrOrReceiverIdEquals(senderId, receiverId)
        ));

        return !connectionRequests.stream().filter(c -> c.getEsitoRichiesta().equals(EsitoRichiesta.ACCETTATA)).toList().isEmpty();

    }

    public ConnectionRequest findById(Long id) {
        return connectionRequestRepository.findById(id).orElseThrow(() -> new BadRequestException("Richiesta non trovata"));
    }

    public ConnectionRequest accept(Long connectionRequestId, User user) {
        ConnectionRequest connectionRequest = findById(connectionRequestId);
        if (connectionRequest.getReceiver().getId() == user.getId()) {
            connectionRequest.setEsitoRichiesta(EsitoRichiesta.ACCETTATA);
            return connectionRequestRepository.save(connectionRequest);
        } else {
            throw new UnauthorizedException("Non hai i permessi per modificare questa richiesta");
        }
    }

    public ConnectionRequest refuse(Long connectionRequestId, User user) {
        ConnectionRequest connectionRequest = findById(connectionRequestId);
        if (connectionRequest.getReceiver().getId() == user.getId()) {
            connectionRequest.setEsitoRichiesta(EsitoRichiesta.RIFIUTATA);
            return connectionRequestRepository.save(connectionRequest);
        } else {
            throw new UnauthorizedException("Non hai i permessi per modificare questa richiesta");
        }
    }

    public ConnectionRequest delete(Long connectionRequestId, User user) {
        ConnectionRequest connectionRequest = findById(connectionRequestId);
        if (connectionRequest.getSender().getId() == user.getId()) {
            connectionRequest.setEsitoRichiesta(EsitoRichiesta.ANNULLATA);
            return connectionRequestRepository.save(connectionRequest);
        } else {
            throw new UnauthorizedException("Non hai i permessi per modificare questa richiesta");
        }
    }

    public boolean deleteFriend(Long visitedUserId, User user) {
        Page<ConnectionRequest> connectionRequest = findByParams(visitedUserId, user.getId(), null, null,
                "ACCETTATA", 0);
        if (connectionRequest.getContent().isEmpty()) {
            connectionRequest = findByParams(user.getId(), visitedUserId, null, null,
                    "ACCETTATA", 0);
        }
        if (connectionRequest.getContent().isEmpty()) {
            throw new BadRequestException("Non è stata trovata nessuna richiesta da annullare");
        }
        connectionRequest.getContent().get(0).setEsitoRichiesta(EsitoRichiesta.ANNULLATA);
        connectionRequestRepository.save(connectionRequest.getContent().get(0));
        return true;
    }
}

