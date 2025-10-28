package com.example.game.punteggio;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PunteggioRepository extends JpaRepository<Punteggio, Long>, JpaSpecificationExecutor<Punteggio> {

    static Specification<Punteggio> partitaId(Long partitaId) {
        if (partitaId == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("partita").get("id"),partitaId);
    }
}
