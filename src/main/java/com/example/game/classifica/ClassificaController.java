package com.example.game.classifica;

import com.example.game.trofeo.Trofeo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/classifiche")
public class ClassificaController {

    @Autowired
    private ClassificaService classificaService;

    @GetMapping("/user")
    private Page<Classifica> getByUserId(@RequestParam Long userId,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(defaultValue = "id") String orderBy,
                                     @RequestParam(defaultValue = "ASC") String sortOrder) {
        return classificaService.getByUserId(userId, page, size, orderBy, sortOrder);
    }

    @GetMapping("/gioco")
    private Classifica getByGiocoId(@RequestParam Long giocoId) {
        return classificaService.getByGiocoId(giocoId).orElse(null);
    }
}
