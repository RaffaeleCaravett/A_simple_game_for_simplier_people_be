package com.example.game.tournament;

import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.TournamentDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tournament")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;


    @PostMapping("")
    @PreAuthorize("hasAuthority('Admin')")
    public Tournament create(@RequestBody @Valid TournamentDTO tournamentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }

        return tournamentService.create(tournamentDTO);
    }

    @GetMapping("")
    public Page<Tournament> getAll(@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
                                   @RequestParam(required = false) String nome,
                                   @RequestParam(required = false) String gioco,
                                   @RequestParam(required = false) LocalDate creazione,
                                   @RequestParam(required = false) LocalDate inizio,
                                   @RequestParam(required = false) LocalDate fine,
                                   @RequestParam(required = false) String stato) {
        return tournamentService.getAll(pageable,nome,gioco,creazione,inizio,fine,stato);
    }

    @GetMapping("/{giocoId}")
    public List<Tournament> getByGiocoId(@PathVariable Long giocoId) {
        return tournamentService.getByGiocoId(giocoId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public boolean deleteById(@PathVariable Long id) {
        return tournamentService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public Tournament putById(@PathVariable Long id, @RequestBody @Valid TournamentDTO tournamentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }

        return tournamentService.put(id, tournamentDTO);
    }

}
