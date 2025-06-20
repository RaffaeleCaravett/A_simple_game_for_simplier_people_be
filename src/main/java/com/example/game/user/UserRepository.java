package com.example.game.user;

import com.example.game.citta.Citta;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByIdAndIsActive(Long id,boolean isActive);
    Optional<User> findByEmailAndIsActive(String email,boolean isActive);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndIsValidated(String email,boolean isValidated);
    Page<User> findByCreatedAtDateBetween(LocalDate dateOne, LocalDate dateTwo, Pageable pageable);
    Page<User> findByNomeContainingIgnoreCaseAndIsActive(String nome,boolean isActive,Pageable pageable);
    Page<User> findByCognomeContainingIgnoreCaseAndIsActive(String cognome,boolean isActive,Pageable pageable);
    Page<User> findByNomeContainingIgnoreCaseAndCognomeContainingIgnoreCaseAndIsActive(String nome,String cognome,boolean isActive,Pageable pageable);
    Page<User> findByCittaAndIsActive(Citta citta,boolean isActive,Pageable pageable);
}
