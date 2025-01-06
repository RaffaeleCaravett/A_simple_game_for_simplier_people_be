package com.example.game.user;

import com.example.game.citta.Citta;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmailAndIsActive(String email,boolean isActive);
    Page<User> findByCreatedAtDateBetween(LocalDate dateOne, LocalDate dateTwo, Pageable pageable);
    Page<User> findByNomeContainingIgnoreCase(String nome,Pageable pageable);
    Page<User> findByCognomeContainingIgnoreCase(String cognome,Pageable pageable);
    Page<User> findByNomeContainingIgnoreCaseAndCognomeContainingIgnoreCase(String nome,String cognome,Pageable pageable);
    Page<User> findByCitta(Citta citta,Pageable pageable);
}
