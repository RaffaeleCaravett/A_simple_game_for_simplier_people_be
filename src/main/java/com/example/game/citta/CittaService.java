package com.example.game.citta;

import com.example.game.exceptions.CityNotFoundException;
import com.example.game.payloads.entities.CittaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CittaService {
    @Autowired
    CittaRepository cittaRepository;

    public List<Citta> findAll() {
        return cittaRepository.findAll();
    }

    public int getResidents(long id) {
        Citta citta = findById(id);
        return citta.getUser().size();
    }

    public Citta findById(long id) {
        return cittaRepository.findById(id).orElseThrow(() -> new CityNotFoundException(id));
    }

    public boolean deleteById(long id) {
        try {
            cittaRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Citta putById(long id, CittaDTO cittaDTO) {
       Citta citta = findById(id);
       citta.setNome(cittaDTO.nome());
       return cittaRepository.save(citta);
    }
    public Citta save(CittaDTO cittaDTO) {
        Citta citta = new Citta();
        citta.setNome(cittaDTO.nome());
        return cittaRepository.save(citta);
    }
}
