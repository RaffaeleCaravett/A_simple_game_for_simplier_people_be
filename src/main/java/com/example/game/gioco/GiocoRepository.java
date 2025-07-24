package com.example.game.gioco;

import com.example.game.recensione.Recensione;
import jakarta.persistence.criteria.*;
import org.apache.catalina.authenticator.jaspic.PersistentProviderRegistrations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface GiocoRepository extends JpaRepository<Gioco, Long>, JpaSpecificationExecutor<Gioco> {
    List<Gioco> findByIsActive(boolean isActive);

    @Query(value = "SELECT g FROM Gioco g " +
            "            JOIN Recensione r ON g.id = r.gioco.id " +
            "            WHERE (g.nomeGioco IS NULL OR UPPER(g.nomeGioco) LIKE CONCAT('%', UPPER(:nomeGioco), '%')) " +
            "            AND (:difficolta IS NULL OR :difficolta < g.difficolta) " +
            "            GROUP BY g.id " +
            "            HAVING AVG(r.punteggio) >= :punteggio ")
    Page<Gioco> findGiochiByFilters(@Param("punteggio") Integer punteggio,
                                    @Param("nomeGioco") String nomeGioco,
                                    @Param("difficolta") Integer difficolta,
                                    @Param("categoria") Long categoria,
                                    Pageable pageable);

    static Specification<Gioco> isActive(Boolean isActive) {
        if (isActive == null) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isActive"), isActive));
    }

    static Specification<Gioco> punteggioMoreThanOrEquals(Integer punteggio) {
        if (punteggio == null) return null;

        return (root, query, criteriaBuilder) -> {
            Join<Gioco, Recensione> giochi = root.join("recensione");
            query.having(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.avg(giochi.get("punteggio")), Double.valueOf(punteggio)));
            query.groupBy(root);
            return criteriaBuilder.and();
        };
    }

    static Specification<Gioco> difficoltaMoreThanOrEquals(Integer difficolta) {
        if (difficolta == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("difficolta"), difficolta);
    }

    static Specification<Gioco> nomeLike(String nome) {
        if (nome == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("nomeGioco")), "%" + nome.toUpperCase() + "%");
    }

    static Specification<Gioco> categoriaIn(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.or(criteriaBuilder.in(root.get("categorie").get("id")).value(ids)));
    }

    Page<Gioco> findAllByUsers_IdAndIsActive(long id, boolean isActive, Pageable pageable);

    Gioco findAllByRecensione_IdAndIsActive(long id, boolean isActive);
}
