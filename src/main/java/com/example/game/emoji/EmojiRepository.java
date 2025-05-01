package com.example.game.emoji;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmojiRepository extends JpaRepository<Emoji,Long> {
    List<Emoji> findAllByTitle(String title);
}
