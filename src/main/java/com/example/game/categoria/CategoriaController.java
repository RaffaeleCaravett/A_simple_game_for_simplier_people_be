package com.example.game.categoria;

import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.CategoriaDTO;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/categoria")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('Admin')")
    public Categoria save(@RequestBody @Validated CategoriaDTO categoriaDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return  categoriaService.save(categoriaDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public Categoria put(@PathVariable Long id, @RequestBody @Validated CategoriaDTO categoriaDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return  categoriaService.putById(categoriaDTO,id);
    }

    @GetMapping("/assignToGame")
    @PreAuthorize("hasAuthority('Admin')")
    public Categoria assignToGame(@RequestParam Long id, @RequestParam Long giocoId) {
        return  categoriaService.assignToGame(id,giocoId);
    }

    @GetMapping("")
    public List<Categoria> getAll(){
        return categoriaService.getAll().stream().sorted(Comparator.comparing(Categoria::getId)).toList();
    }

    @GetMapping("/{name}")
    public List<Categoria> getAllByNameContaining(@PathVariable String name){
        return categoriaService.getByNameContaining(name);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public boolean deleteById(@PathVariable Long id){
        return categoriaService.deleteById(id);
    }
}
