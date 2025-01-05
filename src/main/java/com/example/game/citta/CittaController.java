package com.example.game.citta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/citta")
public class CittaController {
    @Autowired
    CittaService cittaService;
}
