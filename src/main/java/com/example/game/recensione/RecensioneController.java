package com.example.game.recensione;

import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.RecensioneDTO;
import com.example.game.payloads.entities.RecensioneGiocoDTO;
import com.example.game.user.User;
import jakarta.transaction.Transactional;
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
    @PreAuthorize("hasAuthority('User')")
    @Transactional
    public Recensione save(@AuthenticationPrincipal User user, @RequestBody @Validated RecensioneDTO recensioneDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }

        return recensioneService.save(user, recensioneDTO);
    }

    @GetMapping("")
    public Page<RecensioneGiocoDTO> getByUserId(@AuthenticationPrincipal User user,
                                                @RequestParam(required = false, defaultValue = "0") long id,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "id") String orderBy,
                                                @RequestParam(defaultValue = "ASC") String sortOrder) {
        if(id==0) return recensioneService.findAllByUserId(user.getId(), page, size, orderBy, sortOrder);
        else return recensioneService.findAllByUserId(id, page, size, orderBy, sortOrder);
    }

    @GetMapping("/byGiocoId/{id}")
    public Page<Recensione> getByGiocoId(@PathVariable long id,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "4") int size,
                                        @RequestParam(defaultValue = "punteggio") String orderBy,
                                        @RequestParam(defaultValue = "DESC") String sortOrder,
                                         @AuthenticationPrincipal User user) {
        return recensioneService.findAllByGiocoId(id,user.getId(), page, size, orderBy, sortOrder);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('User')")
    public boolean deleteById(@AuthenticationPrincipal User user,
                              @PathVariable long id) {
        return recensioneService.deleteRecensione(user.getId(), id);
    }

    @GetMapping("/punteggio/{punteggio}/{giocoId}")
    @PreAuthorize("hasAuthority('User')")
    public Page<Recensione> getByPunteggioAndGiocoId(@PathVariable int punteggio, @PathVariable long giocoId,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "id") String orderBy,
                                                     @RequestParam(defaultValue = "ASC") String sortOrder) {
        return recensioneService.findAllByGiocoIdAndPunteggio(giocoId, punteggio, page, size, orderBy, sortOrder);
    }

    @GetMapping("/userPunteggio/{punteggio}/{userId}")
    @PreAuthorize("hasAuthority('User')")
    public Page<Recensione> getByPunteggioAndUserId(@PathVariable int punteggio, @PathVariable long userId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "id") String orderBy,
                                                    @RequestParam(defaultValue = "ASC") String sortOrder) {
        return recensioneService.findAllByUserIdAndPunteggio(userId, punteggio, page, size, orderBy, sortOrder);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('User')")
    public Recensione putById(@AuthenticationPrincipal User user, @RequestBody @Validated RecensioneDTO recensioneDTO, BindingResult bindingResult, @PathVariable long id){
        if (bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return recensioneService.putById(user.getId(),id,recensioneDTO);
    }

    @GetMapping("/userAndGioco")
    @PreAuthorize("hasAuthority('User')")
    public Recensione getByUserIdAndGiocoId(@AuthenticationPrincipal User user, @RequestParam long giocoId){
        return recensioneService.findByUserIdAndGiocoId(user.getId(),giocoId);
    }

}
