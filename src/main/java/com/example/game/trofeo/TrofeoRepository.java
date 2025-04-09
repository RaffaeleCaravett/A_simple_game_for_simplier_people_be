package com.example.game.trofeo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrofeoRepository extends JpaRepository<Trofeo,Long> {
    Page<Trofeo> findByUser_Id(long userId, Pageable pageable);
    Page<Trofeo> findByGioco_Id(long giocoId,Pageable pageable);
}
