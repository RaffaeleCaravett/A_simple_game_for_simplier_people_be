package com.example.game.trofeo;

import com.example.game.gioco.Gioco;
import com.example.game.gioco.GiocoService;
import com.example.game.payloads.entities.TrofeoDTO;
import com.example.game.user.User;
import com.example.game.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TrofeoService {

    @Autowired
    private TrofeoRepository trofeoRepository;
    @Autowired
    private GiocoService giocoService;
    @Autowired
    private UserService userService;

    public Trofeo crea(TrofeoDTO trofeoDTO) {
        Gioco gioco = giocoService.findById(trofeoDTO.giocoId());
        User user = userService.findById(trofeoDTO.userId());
        return trofeoRepository.save(Trofeo.builder()
                .gioco(gioco)
                .user(user)
                .createdAt(LocalDate.now().toString())
                .createdAtDate(LocalDate.now())
                .modifiedAt(LocalDate.now().toString())
                .isActive(true)
                .build());
    }

    public Page<Trofeo> getByUserId(Long userId, Integer page, Integer size, String orderBy, String sortOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), orderBy));

        return trofeoRepository.findByUser_Id(userId, pageable);
    }

    public Page<Trofeo> getByGiocoId(Long giocoId, Integer page, Integer size, String orderBy, String sortOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), orderBy));

        return trofeoRepository.findByGioco_Id(giocoId, pageable);
    }
}
