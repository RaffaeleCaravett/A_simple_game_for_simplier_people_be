package com.example.game.partita;

import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.PartitaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/partita")
public class PartitaController {

    @Autowired
    PartitaService partitaService;


    @PostMapping("")
    @PreAuthorize("hasAuthority('User')")
    public List<Partita> save(@RequestBody @Validated List<PartitaDTO> partitaDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return partitaService.save(partitaDTO);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('User')")
    public Page<Partita> getByUserId(@PathVariable long id,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "id") String orderBy,
                                     @RequestParam(defaultValue = "ASC") String sortOrder) {
        return partitaService.getAllByUserId(id, page, size, orderBy, sortOrder);
    }

    @GetMapping("/gioco/{id}")
    @PreAuthorize("hasAuthority('User')")
    public Page<Partita> getByGiocoId(@PathVariable long id,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String orderBy,
                                      @RequestParam(defaultValue = "ASC") String sortOrder) {
        return partitaService.getAllByGiocoId(id, page, size, orderBy, sortOrder);
    }

    @GetMapping("/userAndDate/{id}")
    @PreAuthorize("hasAuthority('User')")
    public Page<Partita> getByUserAndDates(@PathVariable long id,
                                           @RequestParam() @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                           @RequestParam() @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "id") String orderBy,
                                           @RequestParam(defaultValue = "ASC") String sortOrder) {
        return partitaService.getAllByDateBetweenAndUserId(id, from, to, page, size, orderBy, sortOrder);
    }
    @GetMapping("/userAndGioco/{id}/{giocoId}")
    @PreAuthorize("hasAuthority('User')")
    public Page<Partita> getByUserId(@PathVariable long id,
                                     @PathVariable long giocoId,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "id") String orderBy,
                                     @RequestParam(defaultValue = "ASC") String sortOrder) {
        return partitaService.getAllByUserAndGiocoId(id,giocoId, page, size, orderBy, sortOrder);
    }

}
