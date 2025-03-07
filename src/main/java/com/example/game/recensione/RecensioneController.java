package com.example.game.recensione;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recensione")
@RequiredArgsConstructor
public class RecensioneController {

    private final RecensioneService recensioneService;



}
