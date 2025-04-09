package com.example.game.gioco;

import com.example.game.exceptions.BadRequestException;
import com.example.game.exceptions.GiocoNotFoundException;
import com.example.game.user.UserService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class GiocoService {

    private final GiocoRepository giocoRepository;
    private final UserService userService;

    public List<Gioco> findAllByIsActive(boolean isActive) {
        return giocoRepository.findByIsActive(isActive);
    }

    public Gioco findById(long id) {
        return giocoRepository.findById(id).orElseThrow(() -> new GiocoNotFoundException(id));
    }

    public Page<Gioco> findAllByFilters(@Nullable String nomeGioco, @Nullable Integer difficolta, @Nullable Integer punteggio, int page, int size, String orderBy, String sortOrder) {
        orderByIsNotIn(orderBy);
        sortOrderIsNotIn(sortOrder);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), orderBy));

        if (punteggio == 0) {
            return giocoRepository.findByNomeGiocoContainingAndDifficoltaGreaterThan(nomeGioco, difficolta, pageable);
        }
        if (difficolta == 0) {
            difficolta = null;
        }

        return giocoRepository.findGiochiByFilters(punteggio, nomeGioco, difficolta, pageable);
    }

    public void orderByIsNotIn(String orderBy) {
        List<String> attributes = new ArrayList<>();
        attributes.add("id");
        attributes.add("createdAt");
        attributes.add("difficolta");
        attributes.add("recensione");
        attributes.add("nomeGioco");
        if (!attributes.contains(orderBy)) {
            throw new BadRequestException("Non c'è nessun attributo di nome : " + orderBy);
        }
    }

    public void sortOrderIsNotIn(String sortOrder) {
        List<String> directions = new ArrayList<>();
        directions.add("ASC");
        directions.add("DESC");
        if (!directions.contains(sortOrder)) {
            throw new BadRequestException("Non c'è una direzione : " + sortOrder);
        }
    }

    public Page<Gioco> getByUserId(long id, int page, int size, String orderBy, String sortOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), orderBy));

        return giocoRepository.findAllByUsers_IdAndIsActive(id, true, pageable);
    }

    public Gioco getByReceId(long id) {
        return giocoRepository.findAllByRecensione_IdAndIsActive(id, true);
    }

    public boolean assignGiocoToUser(long giocoId, long userId){
        try {
            var gioco = findById(giocoId);
            var user = userService.findById(userId);
            user.addGioco(gioco);
            gioco.addUser(user);
            userService.save(user);
            giocoRepository.save(gioco);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Gioco save(Gioco gioco){
        return giocoRepository.save(gioco);
    }
}
