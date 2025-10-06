package com.example.game.tournament;

import com.example.game.enums.TournamentState;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;

public interface TournamentRepository extends JpaRepository<Tournament, Long>, JpaSpecificationExecutor<Tournament> {

    static Specification<Tournament> nomeLike(String nome) {
        if (nome == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
                "%" + nome.toUpperCase() + "%");
    }

    static Specification<Tournament> nomeEquals(String nome) {
        if (nome == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.upper(criteriaBuilder.trim(root.get("name"))),
                nome.trim().toUpperCase());
    }

    static Specification<Tournament> isActive(Boolean isActive) {
        if (isActive == null) return null;
        if (isActive) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isActive"));
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isActive"));
    }

    static Specification<Tournament> giocoLike(String nome) {
        if (nome == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("gioco").get("nomeGioco")),
                "%" + nome.toUpperCase() + "%");
    }

    static Specification<Tournament> statoEquals(TournamentState state) {
        if (state == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tournamentState"), state);
    }

    static Specification<Tournament> statoNotEquals(TournamentState state) {
        if (state == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("tournamentState"), state);
    }

    static Specification<Tournament> dataCreazioneEquals(LocalDate creationDate) {
        if (creationDate == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("createdAtDate"),
                creationDate);
    }

    static Specification<Tournament> dataInizioEquals(LocalDate startDate) {
        if (startDate == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("startDate"),
                startDate);
    }

    static Specification<Tournament> dataFineEquals(LocalDate endDate) {
        if (endDate == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("endDate"),
                endDate);
    }
}
