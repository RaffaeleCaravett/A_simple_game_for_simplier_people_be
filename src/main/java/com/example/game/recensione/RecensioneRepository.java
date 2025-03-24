package com.example.game.recensione;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecensioneRepository extends JpaRepository<Recensione,Long> {

    Optional<Recensione> findByIdAndIsActive(long id, boolean isActive);
    Optional<Recensione> findByUser_IdAndGioco_IdAndIsActive(long userId, long giocoId,boolean isActive);
    Page<Recensione> findAllByUser_IdAndIsActive(long userId, boolean active, Pageable pageable);
    Page<Recensione> findAllByUser_IdAndPunteggioAndIsActive(long userId,int punteggio, boolean active, Pageable pageable);
    Page<Recensione> findAllByGioco_IdAndPunteggioAndIsActive(long userId,int punteggio, boolean active, Pageable pageable);
    Page<Recensione> findAllByGioco_IdAndIsActiveAndUser_IdNot(long id, boolean isActive,long userId, Pageable pageable);

}
