package com.example.game.trofeo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trofeo")
public class TrofeoController {

    @Autowired
    private TrofeoService trofeoService;

    @GetMapping("/user/{id}")
    private Page<Trofeo> getByUserId(@PathVariable long id,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(defaultValue = "id") String orderBy,
                                     @RequestParam(defaultValue = "ASC") String sortOrder) {
        return trofeoService.getByUserId(id, page, size, orderBy, sortOrder);
    }

    @GetMapping("/gioco")
    private Page<Trofeo> getByGiocoId(@RequestParam Long giocoId,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(defaultValue = "id") String orderBy,
                                     @RequestParam(defaultValue = "ASC") String sortOrder) {
        return trofeoService.getByGiocoId(giocoId, page, size, orderBy, sortOrder);
    }
}
