package com.example.game.blocked;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedRepository extends JpaRepository<Blocked, Long>, JpaSpecificationExecutor<Blocked> {
    static Specification<Blocked> blockedIdEquals(Long blockedId){
        if(blockedId == null) return null;

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("blocked").get("id"),blockedId);
    }
    static Specification<Blocked> blockerIdEquals(Long blockedId){
        if(blockedId == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("blocker").get("id"),blockedId);
    }
    static Specification<Blocked> idEquals(Long id){
        if(id == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"),id);
    }
}
