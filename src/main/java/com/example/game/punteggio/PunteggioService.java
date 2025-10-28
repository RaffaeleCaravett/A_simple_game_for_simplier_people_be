package com.example.game.punteggio;

import com.example.game.partita.Partita;
import jakarta.mail.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PunteggioService {

    @Autowired
    PunteggioRepository punteggioRepository;

    public Punteggio save(Punteggio punteggio) {
        return punteggioRepository.save(punteggio);
    }

    public Punteggio update(Partita partita) {
        Punteggio punteggio = punteggioRepository.findAll(Specification.where(PunteggioRepository.partitaId(partita.getId()))).stream().findFirst().orElse(new Punteggio());
        punteggio.setPunteggio(partita.getPunteggio().getPunteggio());
        if (null == punteggio.getPartita()){
            punteggio.setPartita(partita);
        }
            return punteggioRepository.save(punteggio);
    }
}
