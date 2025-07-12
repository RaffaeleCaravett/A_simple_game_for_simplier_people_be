package com.example.game.categoria;

import com.example.game.exceptions.NotFoundException;
import com.example.game.gioco.Gioco;
import com.example.game.gioco.GiocoService;
import com.example.game.payloads.entities.CategoriaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final GiocoService giocoService;

    public Categoria save(CategoriaDTO categoriaDTO) {
        Categoria categoria = new Categoria();
        categoria.setNome(categoriaDTO.nome());
        return categoriaRepository.save(categoria);
    }

    public Categoria putById(CategoriaDTO categoriaDTO, Long id) {
        Categoria categoria = findById(id);
        categoria.setNome(categoriaDTO.nome());
        return categoriaRepository.save(categoria);
    }

    public Categoria findById(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new NotFoundException("Categoria non trovata con id " + id));
    }

    public Categoria assignToGame(Long categoriaId, Long giocoId) {
        Categoria categoria = findById(categoriaId);
        Gioco gioco = giocoService.findById(giocoId);
        List<Categoria> categorias = gioco.getCategorie();
        List<Gioco> giocos = categoria.getGiochi();
        categorias.add(categoria);
        giocos.add(gioco);
        categoria.setGiochi(giocos);
        gioco.setCategorie(categorias);
        giocoService.save(gioco);
        return categoriaRepository.save(categoria);
    }
    public List<Categoria> getAll() {
        return categoriaRepository.findAll();
    }

    public List<Categoria> getByNameContaining(String name){
       return categoriaRepository.findAll(Specification.where(CategoriaRepository.findByNameLike(name)));
    }

    public boolean deleteById(Long id){
        try {
            categoriaRepository.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
