package com.example.game.preferito;

import com.example.game.exceptions.BadRequestException;
import com.example.game.exceptions.PreferitoNotFoundException;
import com.example.game.gioco.Gioco;
import com.example.game.gioco.GiocoService;
import com.example.game.payloads.entities.PreferitoDTO;
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
public class PreferitoService {


    @Autowired
    PreferitoRepository preferitoRepository;
    @Autowired
    GiocoService giocoService;
    @Autowired
    UserService userService;

    public Preferito save(PreferitoDTO preferitoDTO){
        Gioco gioco = giocoService.findById(preferitoDTO.gioco_id());
        User user = userService.findById(preferitoDTO.user_id());
        if(!user.checkIfPreferitiExists(gioco)){
            return  preferitoRepository.save(Preferito.builder()
                    .gioco(gioco)
                    .user(user)
                    .createdAt(LocalDate.now().toString())
                    .isActive(true)
                    .createdAtDate(LocalDate.now())
                    .modifiedAt(LocalDate.now().toString())
                    .build());
        };
        return null;
    }

    public boolean deletePreferito(long id){
        try {
            Preferito preferito = findById(id);
            preferitoRepository.delete(preferito);
            return true;
        }catch (Exception e){
            return false;
        }
        }


    public Preferito findById(long id){
        return preferitoRepository.findById(id).orElseThrow(()-> new PreferitoNotFoundException("Nessun preferito con id " + id));
    }

    public Page<Preferito> getByUserId(long userId,int page, int size, String sort, String order){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.fromString(order),sort));
        return preferitoRepository.findAllByUser_Id(userId,pageable);
    }
    public Page<Preferito> getByGiocoId(long giocoId,int page, int size, String sort, String order){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.fromString(order),sort));
        return preferitoRepository.findAllByGioco_Id(giocoId,pageable);
    }
}
