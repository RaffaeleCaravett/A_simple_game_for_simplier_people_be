package com.example.game.citta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CittaRepository extends JpaRepository<Citta,Long> {
}
