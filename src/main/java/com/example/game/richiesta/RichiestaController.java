package com.example.game.richiesta;

import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.RichiestaDTO;
import com.example.game.user.User;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/richiesta")
@RequiredArgsConstructor
public class RichiestaController {
    private final RichiestaService richiestaService;


    @PostMapping("")
    public Richiesta save(@RequestBody @Validated RichiestaDTO richiestaDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return richiestaService.save(richiestaDTO);
    }

    @GetMapping("/userId/{id}")
    public Page<Richiesta> getByUserId(@PathVariable Long id, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return richiestaService.getByUserId(id, pageable);
    }
@DeleteMapping("/{id}")
    public Boolean deleteById(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return richiestaService.delete(user, id);
    }

    @GetMapping("/byParams")
    public Page<Richiesta> getRichiesteByParams(@RequestParam(required = false) Long userId,
                                                @RequestParam(required = false) String oggetto,
                                                @RequestParam(required = false) String descrizione,
                                                @RequestParam(required = false) LocalDate from,
                                                @RequestParam(required = false) LocalDate to,
                                                @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return richiestaService.byFilters(descrizione, oggetto, userId, from, to, pageable);
    }

    @PutMapping("/{id}")
    public Richiesta modify(@AuthenticationPrincipal User user,@PathVariable Long id, @RequestBody @Validated RichiestaDTO richiestaDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return richiestaService.modifyRichiesta(user.getId(),richiestaDTO,id);
    }
}



