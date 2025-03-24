package com.example.game.partita;

import com.example.game.enums.Esito;
import com.example.game.gioco.GiocoService;
import com.example.game.payloads.entities.PartitaDTO;
import com.example.game.punteggio.Punteggio;
import com.example.game.punteggio.PunteggioService;
import com.example.game.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PartitaService {

    @Autowired
    private PartitaRepository partitaRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private GiocoService giocoService;
    @Autowired
    private PunteggioService punteggioService;


    public Partita save(PartitaDTO partitaDTO){
        Partita partita = new Partita();
        partita.setUser(userService.findById(partitaDTO.userId()));
        partita.setGioco(giocoService.findById(partitaDTO.giocoId()));
        Punteggio punteggio = new Punteggio();
        punteggio.setPunteggio(partitaDTO.punteggio());
        punteggio.setPartita(partita);
        punteggio.setCreatedAt(LocalDate.now().toString());
        punteggio.setActive(true);
        punteggio.setCreatedAtDate(LocalDate.now());
        partita.setCreatedAt(LocalDate.now().toString());
        partita.setActive(true);
        partita.setCreatedAtDate(LocalDate.now());
        partita.setEsito(Esito.valueOf(partitaDTO.esito()));

        return partitaRepository.save(partita);
    }


    public Page<Partita> getAllByUserId(long userId, int page, int size, String orderBy, String sortOrder){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.fromString(sortOrder),orderBy));
        return partitaRepository.findAllByUser_Id(userId,pageable);
    }

    public Page<Partita> getAllByGiocoId(long giocoId, int page, int size, String orderBy, String sortOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), orderBy));
        return partitaRepository.findAllByGioco_Id(giocoId, pageable);
    }

    public Page<Partita> getAllByDateBetweenAndUserId(long userId,String dataFrom,String dateTo, int page, int size, String orderBy, String sortOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), orderBy));
        return partitaRepository.findAllByUser_IdAndCreatedAtDateBetween(userId,LocalDate.parse(dataFrom),LocalDate.parse(dateTo), pageable);
    }
}
