package com.example.game.classifica;

import com.example.game.gioco.Gioco;
import com.example.game.gioco.GiocoService;
import com.example.game.partita.Partita;
import com.example.game.partita.PartitaRepository;
import com.example.game.partita.PartitaService;
import com.example.game.payloads.entities.ClassificaDTO;
import com.example.game.payloads.entities.ClassificaWithStatisticsDTO;
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
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ClassificaService {
    @Autowired
    private ClassificaRepository classificaRepository;
    @Autowired
    private GiocoService giocoService;
    @Autowired
    private UserService userService;
    @Autowired
    private PartitaRepository partitaRepository;

    public Classifica crea(ClassificaDTO classificaDTO) {
        Gioco gioco = giocoService.findById(classificaDTO.giocoId());
        return classificaRepository.save(Classifica.builder()
                .gioco(gioco)
                .createdAt(LocalDate.now().toString())
                .createdAtDate(LocalDate.now())
                .modifiedAt(LocalDate.now().toString())
                .isActive(true)
                .build());
    }

    public Page<Classifica> getByUserId(Long userId, Integer page, Integer size, String orderBy, String sortOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), orderBy));

        return classificaRepository.findByUsers_Id(userId, pageable);
    }

    public Optional<Classifica> getByGiocoId(Long giocoId) {
        return classificaRepository.findByGioco_Id(giocoId);
    }

    public Classifica save(Classifica classifica) {
        return classificaRepository.save(classifica);
    }

    public ClassificaWithStatisticsDTO findTenBests(Classifica classifica){
        HashMap<User, Integer> userRank = new HashMap<>();
        classifica.getUsers().forEach(
                u -> {
                        List<Partita>  partita = partitaRepository.findAllByUser_IdAndGioco_Id(u.getId(),classifica.getGioco().getId());
                        int totalePunteggio = partita.stream().mapToInt(p-> {
                                if(null!=p.getPunteggio()){
                                    return Integer.parseInt(p.getPunteggio().getPunteggio());
                                }
                            return 0;
                        }).sum();
                        if(userRank.size()<10) userRank.put(u,partita.size()+totalePunteggio);
                }
        );
        return ClassificaWithStatisticsDTO.builder()
                .id(classifica.getId())
                .gioco(classifica.getGioco())
                .users(userRank)
                .build();
    }
}
