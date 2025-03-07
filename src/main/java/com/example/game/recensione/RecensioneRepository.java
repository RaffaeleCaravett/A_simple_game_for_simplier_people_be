package com.example.game.recensione;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecensioneRepository extends JpaRepository<Recensione,Long> {

    Optional<Recensione> findByUser_idAndGiocoId(long userId, long giocoId);
}
