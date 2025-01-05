package com.example.game.citta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CittaService {
    @Autowired
    CittaRepository cittaRepository;
}
