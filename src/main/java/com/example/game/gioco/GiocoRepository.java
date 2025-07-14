package com.example.game.gioco;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GiocoRepository extends JpaRepository<Gioco, Long>, JpaSpecificationExecutor<Gioco> {
    List<Gioco> findByIsActive(boolean isActive);


    static Specification<Gioco> punteggioMoreThanOrEquals(Integer punteggio){
        if(punteggio == null ) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("recensione").get("punteggio"),punteggio);
    }
    static Specification<Gioco> difficoltaMoreThanOrEquals(Integer difficolta){
        if(difficolta == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("difficolta"),difficolta);
    }
    static Specification<Gioco> nomeLike(String nome){
        if(nome == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("nomeGioco")),"%"+nome.toUpperCase() +"%");
    }
    static Specification<Gioco> categoriaIn(List<Long> ids){
        if(ids == null || ids.isEmpty()) return null;
        return ((root, query, criteriaBuilder) ->
           criteriaBuilder.or(criteriaBuilder.in(root.get("categorie").get("id")).value(ids)));
    }

    Page<Gioco> findAllByUsers_IdAndIsActive(long id, boolean isActive, Pageable pageable);
    Gioco findAllByRecensione_IdAndIsActive(long id, boolean isActive);
}
