package com.example.game.descrizione;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/descrizioneUser")
@RequiredArgsConstructor
public class DescrizioneController {

    private final DescrizioneService descrizioneService;
}
