package com.example.game.notification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {

    static Specification<Notification> userId(Long userId) {
        if (userId == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("receiver").get("id"), userId);
    }
}
