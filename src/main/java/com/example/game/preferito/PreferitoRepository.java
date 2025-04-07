package com.example.game.preferito;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferitoRepository extends JpaRepository<Preferito,Long> {
}
