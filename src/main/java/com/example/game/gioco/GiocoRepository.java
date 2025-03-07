package com.example.game.gioco;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiocoRepository extends JpaRepository<Gioco,Long>{
    List<Gioco> findByIsActive(boolean isActive);
}
