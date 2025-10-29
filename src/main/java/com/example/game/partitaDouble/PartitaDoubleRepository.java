package com.example.game.partitaDouble;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartitaDoubleRepository extends JpaRepository<PartitaDouble, Long>, JpaSpecificationExecutor<PartitaDouble> {
    static Specification<PartitaDouble> vincitoreId(Long vincitore) {
        if (vincitore == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("vincitori").get("id"), vincitore);
    }

    static Specification<PartitaDouble> torneoId(Long torneo) {
        if (torneo == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tournament").get("id"), torneo);
    }

    static Specification<PartitaDouble> giocoId(Long gioco) {
        if (gioco == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("gioco").get("id"), gioco);
    }

    static Specification<PartitaDouble> vincitoriIn(List<Long> vincitori) {
        if (vincitori == null || vincitori.isEmpty()) return null;
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(criteriaBuilder.in(root.get("vincitori").get("id")).value(vincitori));
    }

    static Specification<PartitaDouble> partecipantiIn(List<Long> partecipanti) {
        if (partecipanti == null || partecipanti.isEmpty()) return null;
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(criteriaBuilder.in(root.get("partecipanti").get("id")).value(partecipanti));
    }
}
