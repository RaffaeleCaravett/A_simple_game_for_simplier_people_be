package com.example.game.descrizione;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DescrizioneRepository extends JpaRepository<Descrizione,Long>, JpaSpecificationExecutor<Descrizione> {
}
