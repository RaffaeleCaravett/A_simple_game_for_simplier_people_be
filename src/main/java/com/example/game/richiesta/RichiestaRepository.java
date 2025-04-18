package com.example.game.richiesta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RichiestaRepository extends JpaRepository<Richiesta, Long>, JpaSpecificationExecutor<Richiesta> {
    Page<Richiesta> findByUser_Id(Long userId, Pageable pageable);

    static Specification<Richiesta> descrizioneLike(String descrizione) {
        if (descrizione == null) return null;
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("descrizione"), descrizione));
    }

    static Specification<Richiesta> oggettoLike(String oggetto) {
        if (oggetto == null) return null;
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("oggetto"), oggetto));
    }

    static Specification<Richiesta> userIdEquals(Long userId) {
        if (userId == null) return null;
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), userId));
    }

    static Specification<Richiesta> createdAtBetween(LocalDate from, LocalDate to) {
        if (from == null || to == null) return null;
        return ((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("cretedAt"), from, to));
    }

}
