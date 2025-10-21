package com.example.game.gioco;

import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.GiocoDTO;
import com.example.game.user.User;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/gioco")
@RequiredArgsConstructor
public class GiocoController {
    private final GiocoRepository giocoRepository;
    private final GiocoService giocoService;

    @GetMapping("")
    public List<Gioco> getAll(@RequestParam(defaultValue = "true") boolean isActive) {
        return giocoService.findAllByIsActive(isActive);
    }

    @GetMapping("/{id}")
    public Gioco getGiocoById(@PathVariable Long id) {
        return giocoService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public Gioco updateGiocoImage(@Nullable @RequestPart(name = "gioco_image", required = false) MultipartFile multipartFile,
                                  @RequestPart(name = "gioco") @Validated GiocoDTO giocoDTO, BindingResult bindingResult,
                                  @PathVariable long id) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        var gioco = giocoRepository.findById(id).orElseThrow();
        try {
            if (multipartFile != null) {
                byte[] fileBytes = multipartFile.getBytes();
                gioco.setImage(fileBytes);
            }
            if (giocoDTO != null) {
                giocoService.put(gioco, giocoDTO);
            }
            return giocoRepository.save(gioco);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return gioco;
        }
    }

    @GetMapping("/byFilters")
    @Transactional
    public Page<Gioco> getGiochiByValidationsAvg(@RequestParam(required = false) String nomeGioco,
                                                 @RequestParam(required = false) Integer difficolta,
                                                 @RequestParam(required = false) Integer avg,
                                                 @RequestParam(required = false) List<Long> categorie,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "id") String orderBy,
                                                 @RequestParam(defaultValue = "ASC") String sortOrder,
                                                 @RequestParam(defaultValue = "true") Boolean isActive) {

        return giocoService.findAllByFilters(nomeGioco, difficolta, avg, categorie, page, size, orderBy, sortOrder, isActive);
    }

    @GetMapping("/userId")
    public Page<Gioco> getByUserId(@RequestParam(required = false) Long id,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "id") String orderBy,
                                   @RequestParam(defaultValue = "ASC") String sortOrder,
                                   @AuthenticationPrincipal User user) {
        if (id == null) {
            id = user.getId();
        }
        return giocoService.getByUserId(id, page, size, orderBy, sortOrder);
    }

    @GetMapping("/assignGiocoToUser")
    public boolean assignGiocoToUser(@RequestParam long gioco, @RequestParam long user) {
        return giocoService.assignGiocoToUser(gioco, user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public boolean assignGiocoToUser(@PathVariable Long id) {
        return giocoService.delete(id);
    }

    @GetMapping("/restore/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public boolean restoreGioco(@PathVariable Long id) {
        return giocoService.restore(id);
    }

    @GetMapping("/categoria/{id}/{categoriaId}")
    @PreAuthorize("hasAuthority('Admin')")
    public Gioco removeCategoria(@PathVariable Long id, @PathVariable Long categoriaId) {
        return giocoService.deleteCategoriasFromGame(categoriaId, id);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('Admin')")
    public Gioco save(@RequestPart(name = "gioco_image") MultipartFile multipartFile,
                      @RequestPart(name = "gioco") @Validated GiocoDTO giocoDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        try {
            return giocoService.create(giocoDTO, multipartFile);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }
}
