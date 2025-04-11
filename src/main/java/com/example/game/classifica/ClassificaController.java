package com.example.game.classifica;

import com.example.game.trofeo.Trofeo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/classifiche")
public class ClassificaController {

    @Autowired
    private ClassificaService classificaService;

    @GetMapping("/user/{id}")
    private Page<Classifica> getByUserId(@PathVariable Long id,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(defaultValue = "id") String orderBy,
                                     @RequestParam(defaultValue = "ASC") String sortOrder) {
        return classificaService.getByUserId(id, page, size, orderBy, sortOrder);
    }

    @GetMapping("/gioco")
    private Classifica getByGiocoId(@RequestParam Long giocoId) {
        return classificaService.getByGiocoId(giocoId).orElse(null);
    }
}
