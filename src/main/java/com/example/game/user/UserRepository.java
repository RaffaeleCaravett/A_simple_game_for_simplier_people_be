package com.example.game.user;

import com.example.game.citta.Citta;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByIdAndIsActive(Long id, boolean isActive);

    Optional<User> findByEmailAndIsActive(String email, boolean isActive);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndIsValidated(String email, boolean isValidated);

    Page<User> findByCreatedAtDateBetween(LocalDate dateOne, LocalDate dateTwo, Pageable pageable);

    Page<User> findByNomeContainingIgnoreCaseAndIsActive(String nome, boolean isActive, Pageable pageable);

    Page<User> findByCognomeContainingIgnoreCaseAndIsActive(String cognome, boolean isActive, Pageable pageable);

    Page<User> findByNomeContainingIgnoreCaseAndCognomeContainingIgnoreCaseAndIsActive(String nome, String cognome, boolean isActive, Pageable pageable);

    Page<User> findByCittaAndIsActive(Citta citta, boolean isActive, Pageable pageable);

    static Specification<User> isConnected(@Nullable Boolean connected) {
        if (connected == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isConnected"), connected);
    }

    static Specification<User> isActive(@Nullable Boolean active) {
        if (active == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), active);
    }

    static Specification<User> idNotEqual(@Nullable Long id) {
        if (id == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("id"), id);
    }
}
