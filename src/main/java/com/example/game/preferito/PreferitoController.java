package com.example.game.preferito;

import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.PreferitoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/preferito")
@RequiredArgsConstructor
public class PreferitoController {

    private final PreferitoService preferitoService;


    @GetMapping("/user/{id}")
    public Page<Preferito> getAllByUserIdAndParams(@PathVariable long id,
                                                   @RequestParam(required = false) String giocoName,
                                                   @RequestParam(required = false) Integer giocoDifficolta,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "id") String orderBy,
                                                   @RequestParam(defaultValue = "ASC") String sortOrder) {
        return preferitoService.getByUserId(id, giocoName, giocoDifficolta, page, size, orderBy, sortOrder);
    }

    @GetMapping("/gioco/{id}")
    public Page<Preferito> getAllByGiocoId(@PathVariable long id,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "id") String orderBy,
                                           @RequestParam(defaultValue = "ASC") String sortOrder) {
        return preferitoService.getByGiocoId(id, page, size, orderBy, sortOrder);
    }

    @PostMapping("")
    public Preferito save(@RequestBody @Validated PreferitoDTO preferitoDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return preferitoService.save(preferitoDTO);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable long id) {
        return preferitoService.deletePreferito(id);
    }
}
