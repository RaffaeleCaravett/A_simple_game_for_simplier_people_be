package com.example.game.gioco;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class GiocoService {


    private final GiocoRepository giocoRepository;


    public List<Gioco> findAllByIsActive(boolean isActive){
        return giocoRepository.findByIsActive(isActive);
    }
}
