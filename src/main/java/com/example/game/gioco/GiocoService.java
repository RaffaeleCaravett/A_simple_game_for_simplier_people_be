package com.example.game.gioco;

import com.example.game.exceptions.GiocoNotFoundException;
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

    public Gioco findById(long id){
        return giocoRepository.findById(id).orElseThrow(()->new GiocoNotFoundException(id));
    }
}
