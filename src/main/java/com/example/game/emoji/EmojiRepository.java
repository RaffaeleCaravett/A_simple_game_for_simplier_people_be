package com.example.game.emoji;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmojiRepository extends JpaRepository<Emoji,Long>, JpaSpecificationExecutor<Emoji> {
    static Specification<Emoji> titleLike(String title){
        if(title==null) return null;
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"),"%"+title+"%");
    }
}
