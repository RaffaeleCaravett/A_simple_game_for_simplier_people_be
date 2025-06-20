package com.example.game.descrizione;

import com.example.game.payloads.entities.DescrizioneDTO;
import com.example.game.user.User;
import com.example.game.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class DescrizioneService {
    private final DescrizioneRepository descrizioneRepository;

    @Transactional
    public User updateDescrizione(User user, DescrizioneDTO descrizioneDTO) {
        var descrizione = new Descrizione();
        if (null == user.getDescrizione()) {
            descrizione = Descrizione.builder()
                    .textAlignment(descrizioneDTO.textAlignmet())
                    .innerHTML(descrizioneDTO.innerHTML())
                    .user(user)
                    .build();
        } else {
            descrizione = user.getDescrizione();
            descrizione.setInnerHTML(descrizioneDTO.innerHTML());
            descrizione.setTextAlignment(descrizioneDTO.textAlignmet());
        }
        return descrizioneRepository.save(descrizione).getUser();
    }
}
