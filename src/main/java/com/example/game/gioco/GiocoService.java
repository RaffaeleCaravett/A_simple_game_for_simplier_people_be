package com.example.game.gioco;

import com.example.game.categoria.Categoria;
import com.example.game.categoria.CategoriaRepository;
import com.example.game.categoria.CategoriaService;
import com.example.game.exceptions.BadRequestException;
import com.example.game.exceptions.GiocoNotFoundException;
import com.example.game.exceptions.NotFoundException;
import com.example.game.payloads.entities.GiocoDTO;
import com.example.game.payloads.entities.GiocoIdAndNameDTO;
import com.example.game.user.UserService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
@Validated
@RequiredArgsConstructor
public class GiocoService {

    private final GiocoRepository giocoRepository;
    private final UserService userService;
    private final CategoriaRepository categoriaRepository;

    public void put(Gioco gioco, GiocoDTO giocoDTO) {
        if (null != giocoDTO.nome() && !giocoDTO.nome().isEmpty() && !giocoDTO.nome().isBlank()) {
            gioco.setNomeGioco(giocoDTO.nome());
        }
        if (null != giocoDTO.descrizione() && !giocoDTO.descrizione().isEmpty() && !giocoDTO.descrizione().isBlank()) {
            gioco.setDescrizione(giocoDTO.descrizione());
        }
        if (giocoDTO.difficolta() != null) {
            gioco.setDifficolta(giocoDTO.difficolta());
        }
        if (gioco.getCategorie().size() < 3) {
            if (null != giocoDTO.categorie() && !giocoDTO.categorie().isEmpty()) {
                for (Long c : giocoDTO.categorie()) {
                    if (!gioco.getCategorie().stream().map(Categoria::getId).toList().contains(c)) {
                        Categoria categoria = categoriaRepository.findById(c).orElseThrow(() -> new NotFoundException("Categoria non trovata."));
                        if (gioco.getCategorie().size() < 3) {
                            gioco.getCategorie().add(categoria);
                            categoria.getGiochi().add(gioco);
                            categoriaRepository.save(categoria);
                        }
                    }
                }
            }
        } else {
            throw new BadRequestException(gioco.getNomeGioco() + " ha già 3 categorie assegnate, che sono il massimo numero possibile.");
        }
        gioco.setModifiedAt(LocalDate.now().toString());
        giocoRepository.save(gioco);
    }

    public List<Gioco> findAllByIsActive(boolean isActive) {
        return giocoRepository.findByIsActive(isActive);
    }

    public Gioco findById(long id) {
        return giocoRepository.findById(id).orElseThrow(() -> new GiocoNotFoundException(id));
    }

    public Page<Gioco> findAllByFilters(@Nullable String nomeGioco, @Nullable Integer difficolta, @Nullable Integer punteggio,
                                        @Nullable List<Long> categorie, int page, int size, String orderBy, String sortOrder, Boolean isActive) {
        orderByIsNotIn(orderBy);
        sortOrderIsNotIn(sortOrder);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), orderBy));

        return giocoRepository.findAll(Specification.where(GiocoRepository.difficoltaMoreThanOrEquals(difficolta))
                .and(GiocoRepository.nomeLike(nomeGioco))
                .and(GiocoRepository.punteggioMoreThanOrEquals(punteggio))
                .and(GiocoRepository.categoriaIn(categorie))
                .and(GiocoRepository.isActive(isActive)), pageable);
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

    public boolean assignGiocoToUser(long giocoId, long userId) {
        try {
            var gioco = findById(giocoId);
            var user = userService.findById(userId);
            user.addGioco(gioco);
            gioco.addUser(user);
            userService.save(user);
            giocoRepository.save(gioco);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Gioco save(Gioco gioco) {
        return giocoRepository.save(gioco);
    }

    public boolean delete(Long id) {
        try {
            Gioco gioco = findById(id);
            gioco.setDeletedAt(LocalDate.now().toString());
            gioco.setActive(false);
            giocoRepository.save(gioco);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean restore(Long id) {
        try {
            Gioco gioco = findById(id);
            gioco.setDeletedAt(null);
            gioco.setActive(true);
            gioco.setModifiedAt(LocalDate.now().toString());
            giocoRepository.save(gioco);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Gioco deleteCategoriasFromGame(Long categoriaId, Long giocoId) {
        Gioco gioco = findById(giocoId);
        if (gioco.getCategorie().isEmpty())
            throw new BadRequestException(gioco.getNomeGioco() + " non ha categorie assegnate");
        Categoria categoria = categoriaRepository.findById(categoriaId).orElseThrow(() -> new NotFoundException("Categoria non trovare"));
        if (gioco.getCategorie().stream().map(Categoria::getId).toList().contains(categoriaId)) {
            gioco.getCategorie().remove(categoria);
            categoria.getGiochi().remove(gioco);
            categoriaRepository.save(categoria);

        }
        return giocoRepository.save(gioco);
    }

    public Gioco create(GiocoDTO giocoDTO, MultipartFile multipartFile) throws IOException {
        if (null != giocoDTO.categorie() && giocoDTO.categorie().size() > 3) {
            throw new BadRequestException("Puoi inserire un massimo di 3 categorie");
        }
        byte[] fileBytes = multipartFile.getBytes();
        Gioco gioco =
                giocoRepository.save(Gioco.builder().nomeGioco(giocoDTO.nome()).difficolta(null != giocoDTO.difficolta() ? giocoDTO.difficolta() : 1)
                        .descrizione(giocoDTO.descrizione()).createdAt(LocalDate.now().toString()).createdAtDate(LocalDate.now()).isActive(true).image(fileBytes)
                        .build());
        giocoRepository.save(gioco);
        gioco.setCategorie(new ArrayList<>());
        if (null != giocoDTO.categorie() && !giocoDTO.categorie().isEmpty()) {
            for (Long c : giocoDTO.categorie()) {
                Categoria categoria = categoriaRepository.findById(c).orElseThrow(() -> new NotFoundException("Categoria non trovata."));
                if (gioco.getCategorie().size() < 3) {
                    gioco.getCategorie().add(categoria);
                }
                categoria.getGiochi().add(gioco);
                categoriaRepository.save(categoria);
            }
        }
        return giocoRepository.save(gioco);
    }

    public List<GiocoIdAndNameDTO> getIdsAndNames(){
        return giocoRepository.findAll(Specification.where(GiocoRepository.isActive(true))).stream().map(g->GiocoIdAndNameDTO.builder().name(g.getNomeGioco()).id(g.getId()).build()).toList();
    }
}
