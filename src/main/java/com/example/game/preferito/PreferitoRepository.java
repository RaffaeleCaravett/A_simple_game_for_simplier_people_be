package com.example.game.preferito;

import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferitoRepository extends JpaRepository<Preferito,Long>, JpaSpecificationExecutor<Preferito> {
    Page<Preferito> findAllByGioco_Id(long giocoId, Pageable pageable);

    static Specification<Preferito> userIdEquals(@Nullable  Long userId){
        if(userId == null) return null;
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"),userId));
    }
    static Specification<Preferito> giocoNameEquals(@Nullable String giocoName){
        if(giocoName == null) return null;
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("gioco").get("nomeGioco"),giocoName));
    }
    static Specification<Preferito> giocoDifficoltaEquals(@Nullable Integer giocoDifficolta){
        if(giocoDifficolta == null) return null;
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("gioco").get("difficolta"),giocoDifficolta));
    }
    static Specification<Preferito> giocoActiveEquals(@Nullable Boolean isActive){
        if(isActive == null) return null;
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("gioco").get("isActive"),isActive));
    }
}
