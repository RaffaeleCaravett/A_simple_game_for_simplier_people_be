package com.example.game.citta;

import com.example.game.payloads.entities.CittaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/citta")
public class CittaController {
    @Autowired
    CittaService cittaService;

    @GetMapping("")
    public List<Citta> findAll() {
        return cittaService.findAll();
    }

    @GetMapping("/getResidents/{cityId}")
    public int getNumberOfResidents(@PathVariable long id) {
        return cittaService.getResidents(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public boolean deleteById(@PathVariable long id) {
        return cittaService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public Citta putById(@PathVariable long id, @RequestBody @Validated CittaDTO cittaDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return BadRequestException(bindingResult.getAllErrors());
        }
        return cittaService.putById(id,cittaDTO);
    }
}
