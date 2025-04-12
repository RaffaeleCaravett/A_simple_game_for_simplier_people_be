package com.example.game.classifica;

import com.example.game.payloads.entities.ClassificaWithStatisticsDTO;
import com.example.game.trofeo.Trofeo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/classifiche")
public class ClassificaController {

    @Autowired
    private ClassificaService classificaService;

    @GetMapping("/user/{id}")
    public Page<Classifica> getByUserId(@PathVariable long id,
                                        @RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(defaultValue = "id") String orderBy,
                                        @RequestParam(defaultValue = "ASC") String sortOrder) {
        return classificaService.getByUserId(id, page, size, orderBy, sortOrder);
    }

    @GetMapping("/gioco")
    public ClassificaWithStatisticsDTO getByGiocoId(@RequestParam Long giocoId) {
        Optional<Classifica> classifica = classificaService.getByGiocoId(giocoId);
        return classifica.map(value -> classificaService.findTenBests(value)).orElse(null);
    }
}
