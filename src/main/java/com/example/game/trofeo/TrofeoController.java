package com.example.game.trofeo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trofeo")
public class TrofeoController {

    @Autowired
    private TrofeoService trofeoService;

    @GetMapping("/user")
    private Page<Trofeo> getByUserId(@RequestParam Long userId,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(defaultValue = "id") String orderBy,
                                     @RequestParam(defaultValue = "ASC") String sortOrder) {
        return trofeoService.getByUserId(userId, page, size, orderBy, sortOrder);
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
