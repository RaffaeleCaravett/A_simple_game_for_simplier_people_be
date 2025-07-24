package com.example.game.socket.chat;

import com.example.game.user.User;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.swing.text.html.parser.Entity;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

public interface ChatRepository extends JpaRepository<Chat, Long>, JpaSpecificationExecutor<Chat> {
    static Specification<Chat> userIdEqual(@Nullable Long userId) {
        if (userId == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"),userId);
    }
    static Specification<Chat> isActive(@Nullable Boolean active) {
        if (active == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"),active);
    }


}
