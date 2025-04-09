package com.example.game.classifica;

import com.example.game.gioco.Gioco;
import com.example.game.gioco.GiocoService;
import com.example.game.payloads.entities.ClassificaDTO;
import com.example.game.payloads.entities.TrofeoDTO;
import com.example.game.trofeo.Trofeo;
import com.example.game.trofeo.TrofeoRepository;
import com.example.game.user.User;
import com.example.game.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClassificaService {
    @Autowired
    private ClassificaRepository classificaRepository;
    @Autowired
    private GiocoService giocoService;
    @Autowired
    private UserService userService;

    public Classifica crea(ClassificaDTO classificaDTO) {
        Gioco gioco = giocoService.findById(classificaDTO.giocoId());
        return classificaRepository.save(Classifica.builder()
                .gioco(gioco)
                .build());
    }

    public Page<Classifica> getByUserId(Long userId, Integer page, Integer size, String orderBy, String sortOrder){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.fromString(sortOrder),orderBy));

        return classificaRepository.findByUsers_Id(userId,pageable);
    }
    public Optional<Classifica> getByGiocoId(Long giocoId){
        return classificaRepository.findByGioco_Id(giocoId);
    }

    public Classifica save(Classifica classifica){
        return classificaRepository.save(classifica);
    }
}
