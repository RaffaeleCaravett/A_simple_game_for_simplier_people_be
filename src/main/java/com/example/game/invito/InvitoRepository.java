package com.example.game.invito;

import com.example.game.enums.InviteState;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.Instant;

public interface InvitoRepository extends JpaRepository<Invito, Long>, JpaSpecificationExecutor<Invito> {

    static Specification<Invito> receiverIdEquals(Long receiverId) {
        if (receiverId == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("receiver").get("id"), receiverId);
    }
    static Specification<Invito> senderIdEquals(Long senderId) {
        if (senderId == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("receiver").get("id"), senderId);
    }
    static Specification<Invito> stateEquals(InviteState inviteState) {
        if (inviteState == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("inviteState"), inviteState);
    }
    static Specification<Invito> giocoIdEquals(Long giocoId) {
        if (giocoId == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("gioco").get("id"), giocoId);
    }
    static Specification<Invito> isValid(Instant instant) {
        if (instant == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("createdAt"), instant);
    }

}
