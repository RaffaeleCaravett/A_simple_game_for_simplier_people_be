package com.example.game.gioco;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GiocoRepository extends JpaRepository<Gioco, Long> {
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
                                    Pageable pageable);

    Page<Gioco> findByNomeGiocoContainingAndDifficoltaGreaterThan(String nomeGioco, Integer difficolta, Pageable pageable);

    Page<Gioco> findAllByUsers_IdAndIsActive(long id, boolean isActive, Pageable pageable);
    Gioco findAllByRecensione_IdAndIsActive(long id, boolean isActive);
}
