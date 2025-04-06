package com.example.game.gioco;

import com.example.game.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping("/{id}")
    public Gioco updateGiocoImage(@RequestParam(name = "gioco_image") MultipartFile multipartFile, @PathVariable long id) {
        var gioco = giocoRepository.findById(id).orElseThrow();
        try {
            byte[] fileBytes = multipartFile.getBytes();
            gioco.setImage(fileBytes);
            return giocoRepository.save(gioco);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return gioco;
        }
    }

    @GetMapping("/byFilters")
    @PreAuthorize("hasAuthority('User')")
    @Transactional
    public Page<Gioco> getGiochiByValidationsAvg(@RequestParam(required = false) String nomeGioco,
                                                 @RequestParam(required = false) Integer difficolta,
                                                 @RequestParam(required = false) int avg,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "id") String orderBy,
                                                 @RequestParam(defaultValue = "ASC") String sortOrder) {
        return giocoService.findAllByFilters(nomeGioco, difficolta, avg, page, size, orderBy, sortOrder);
    }

    @GetMapping("/userId")
    @PreAuthorize("hasAuthority('User')")
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
    @PreAuthorize("hasAuthority('User')")
    public boolean assignGiocoToUser(@RequestParam long gioco, @RequestParam long user) {
        return giocoService.assignGiocoToUser(gioco, user);
    }
}
