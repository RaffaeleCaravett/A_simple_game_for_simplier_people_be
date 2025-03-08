package com.example.game.recensione;

import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.RecensioneDTO;
import com.example.game.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recensione")
@RequiredArgsConstructor
public class RecensioneController {

    private final RecensioneService recensioneService;


    @PostMapping("")
    @PreAuthorize("hasAuthority('user')")
    public Recensione save(@AuthenticationPrincipal User user, @RequestBody @Validated RecensioneDTO recensioneDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }

        return recensioneService.save(user, recensioneDTO);
    }

    @GetMapping("")
    public Page<Recensione> getByUserId(@AuthenticationPrincipal User user,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "id") String orderBy,
                                        @RequestParam(defaultValue = "ASC") String sortOrder) {
        return recensioneService.findAllByUserId(user.getId(), page, size, orderBy, sortOrder);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user')")
    public boolean deleteById(@AuthenticationPrincipal User user,
                              @PathVariable long id) {
        return recensioneService.deleteRecensione(user.getId(), id);
    }

    @GetMapping("/punteggio/{punteggio}/{giocoId}")
    @PreAuthorize("hasAuthority('user')")
    public Page<Recensione> getByPunteggioAndGiocoId(@PathVariable int punteggio, @PathVariable long giocoId,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "id") String orderBy,
                                                     @RequestParam(defaultValue = "ASC") String sortOrder) {
        return recensioneService.findAllByGiocoIdAndPunteggio(giocoId, punteggio, page, size, orderBy, sortOrder);
    }

    @GetMapping("/userPunteggio/{punteggio}/{userId}")
    @PreAuthorize("hasAuthority('user')")
    public Page<Recensione> getByPunteggioAndUserId(@PathVariable int punteggio, @PathVariable long userId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "id") String orderBy,
                                                    @RequestParam(defaultValue = "ASC") String sortOrder) {
        return recensioneService.findAllByUserIdAndPunteggio(userId, punteggio, page, size, orderBy, sortOrder);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user')")
    public Recensione putById(@AuthenticationPrincipal User user, @RequestBody @Validated RecensioneDTO recensioneDTO, BindingResult bindingResult, @PathVariable long id){
        if (bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return recensioneService.putById(user.getId(),id,recensioneDTO);
    }

}
