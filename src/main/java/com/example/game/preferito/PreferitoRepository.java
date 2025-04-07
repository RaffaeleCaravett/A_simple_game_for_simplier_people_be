package com.example.game.preferito;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferitoRepository extends JpaRepository<Preferito,Long> {
    Page<Preferito> findAllByUser_Id(long userId, Pageable pageable);
    Page<Preferito> findAllByGioco_Id(long giocoId, Pageable pageable);
}
