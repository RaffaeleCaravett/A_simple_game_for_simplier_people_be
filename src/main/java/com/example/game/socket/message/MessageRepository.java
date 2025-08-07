package com.example.game.socket.message;

import com.example.game.enums.MessageState;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Messaggio, Long>, JpaSpecificationExecutor<Messaggio> {
    static Specification<Messaggio> chatIdEquals(@Nullable Long chatId) {
        if (chatId == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("chat").get("id"), chatId);
    }

    static Specification<Messaggio> receiversContain(@Nullable Long userId) {
        if (userId == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("receivers").get("id"), userId);
    }

    static Specification<Messaggio> stateEquals(MessageState messageState) {
        if (messageState == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("state"), messageState);
    }

    @Modifying
    @Query(value = "update message set state = MessageState.READ "
            + " where chat.id = ?1 and sender.id = ?2 and state is MessageState.SENT", nativeQuery = true)
    void sendReadReceipt(Long chatId, Long userId);
}
