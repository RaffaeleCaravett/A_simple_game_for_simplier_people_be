package com.example.game.partita;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PartitaRepository extends JpaRepository<Partita, Long> {
    Page<Partita> findAllByUser_Id(long userId, Pageable pageable);
    Page<Partita> findAllByUser_IdAndGioco_Id(long userId,long giocoId, Pageable pageable);
    List<Partita> findAllByUser_IdAndGioco_Id(long userId, long giocoId);
    Page<Partita> findAllByUser_IdAndCreatedAtDateBetween(long userId, LocalDate from, LocalDate to, Pageable pageable);
    Page<Partita> findAllByGioco_Id(long giocoId, Pageable pageable);

}
