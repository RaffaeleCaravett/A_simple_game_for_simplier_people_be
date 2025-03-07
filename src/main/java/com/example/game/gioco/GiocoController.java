package com.example.game.gioco;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/gioco")
@Validated
@RequiredArgsConstructor
public class GiocoController {

    private final GiocoRepository giocoRepository;
private final GiocoService giocoService;
    @GetMapping("")
    public List<Gioco> getAll(@RequestParam(defaultValue = "true") boolean isActive){
        return giocoService.findAllByIsActive(isActive);
    }

    @PutMapping("/{id}")
    public Gioco updateGiocoImage(@RequestParam(name = "gioco_image") MultipartFile multipartFile,@PathVariable long id){
        var gioco = giocoRepository.findById(id).orElseThrow();
        try {
            byte[] fileBytes = multipartFile.getBytes();
            gioco.setImage(fileBytes);
            return giocoRepository.save(gioco);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return gioco;
        }
    }
}
