package com.example.game.socket.message.messageImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageImageRepository extends JpaRepository<MessageImage, Long>, JpaSpecificationExecutor<MessageImage> {
}
