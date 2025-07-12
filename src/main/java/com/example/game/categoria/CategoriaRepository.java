package com.example.game.categoria;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria,Long>, JpaSpecificationExecutor<Categoria> {
    static Specification<Categoria> findByNameLike(String name){
        if(name == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("nome")),"%"+name.toUpperCase() + "%");
    }
}
