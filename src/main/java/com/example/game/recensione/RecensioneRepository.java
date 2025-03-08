package com.example.game.recensione;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecensioneRepository extends JpaRepository<Recensione,Long> {

    Optional<Recensione> findByIdAndIsActive(long id, boolean isActive);
    Optional<Recensione> findByUser_idAndGiocoId(long userId, long giocoId);
    Page<Recensione> findAllByUser_idAndIsActive(long userId, boolean active, Pageable pageable);
    Page<Recensione> findAllByUser_idAndPunteggioAndIsActive(long userId,int punteggio, boolean active, Pageable pageable);
    Page<Recensione> findAllByGioco_idAndPunteggioAndIsActive(long userId,int punteggio, boolean active, Pageable pageable);

}
