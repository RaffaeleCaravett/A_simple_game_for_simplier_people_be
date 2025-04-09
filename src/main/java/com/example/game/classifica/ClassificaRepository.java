package com.example.game.classifica;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassificaRepository extends JpaRepository<Classifica,Long> {
    Optional<Classifica> findByGioco_Id(long giocoId);
    Page<Classifica> findByUsers_Id(long userId, Pageable pageable);

}
