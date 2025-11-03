package com.example.game.partitaDouble;

import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.PartitaDoubleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partitaDouble")
@RequiredArgsConstructor
public class PartitaDoubleController {
    private final PartitaDoubleService partitaDoubleService;


    @PostMapping("")
    public PartitaDouble create(@RequestBody @Validated PartitaDoubleDTO partitaDoubleDTO, BindingResult bindingResult) {
        return partitaDoubleService.create(partitaDoubleDTO);
    }

    @GetMapping("")
    public Page<PartitaDouble> get(@RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(required = false) Long gioco,
                                   @RequestParam(required = false) Long tournament,
                                   @RequestParam(required = false) List<Long> vincenti,
                                   @RequestParam(required = false) List<Long> partecipanti
    ) {
        return partitaDoubleService.get(page, size, partecipanti, vincenti, tournament, gioco);
    }

    @GetMapping("/{id}")
    public PartitaDouble findById(Long id) {
        return partitaDoubleService.findById(id);
    }

    @GetMapping("/win/{id}/{loserPoints}/{winnerPoints}")
    public PartitaDouble win(@PathVariable Long id, @PathVariable String loserPoints, @PathVariable String winnerPoints, @RequestParam List<Long> vincenti) {
        return partitaDoubleService.win(id, vincenti, loserPoints, winnerPoints);
    }

    @PutMapping("/{id}")
    public PartitaDouble put(@PathVariable Long id, @RequestBody @Validated PartitaDoubleDTO partitaDoubleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return partitaDoubleService.put(id, partitaDoubleDTO);
    }
}
