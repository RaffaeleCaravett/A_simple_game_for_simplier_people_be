package com.example.game.connectionRequest;

import com.example.game.enums.EsitoRichiesta;
import com.sun.tools.jconsole.JConsoleContext;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Long>, JpaSpecificationExecutor<ConnectionRequest> {
    static Specification<ConnectionRequest> sendeIdrOrReceiverIdEquals(Long userId, Long requestUserId) {
        if (userId == null || requestUserId == null) return null;
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("sender").get("id"), userId),
                            criteriaBuilder.equal(root.get("receiver").get("id"),requestUserId)
                        ),
                        criteriaBuilder.and(
                                criteriaBuilder.equal(root.get("sender").get("id"), requestUserId),
                                criteriaBuilder.equal(root.get("receiver").get("id"),userId)
                        )
                );
    }

    static Specification<ConnectionRequest> senderIdEquals(Long userId) {
        if (userId == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("sender").get("id"), userId);
    }

    static Specification<ConnectionRequest> receiverIdEquals(Long userId) {
        if (userId == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("receiver").get("id"), userId);
    }

    static Specification<ConnectionRequest> senderFullnameLike(String fullname) {
        if (fullname == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("sender").get("fullName")), "%" + fullname.toUpperCase() + "%");
    }

    static Specification<ConnectionRequest> receiverFullnameLike(String fullname) {
        if (fullname == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("receiver").get("fullName")), "%" + fullname.toUpperCase() + "%");
    }

    static Specification<ConnectionRequest> stateEquals(EsitoRichiesta esitoRichiesta) {
        if (esitoRichiesta == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("esitoRichiesta"), esitoRichiesta);
    }
}
