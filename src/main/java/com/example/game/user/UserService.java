package com.example.game.user;

import com.example.game.citta.Citta;
import com.example.game.citta.CittaService;
import com.example.game.exceptions.InvalidParamsException;
import com.example.game.exceptions.UserWithEmailNotFoundException;
import com.example.game.exceptions.UserWithIdNotFoundException;
import com.example.game.payloads.entities.UserSignupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CittaService cittaService;


    public Page<User> findByParamsAndIsActive(String nome, String cognome,int page, int size,String orderBy, String direction){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.valueOf(direction),orderBy));
        if(!"".equals(nome)&&!"".equals(cognome)){
            return userRepository.findByNomeContainingIgnoreCaseAndCognomeContainingIgnoreCaseAndIsActive(nome,cognome,true,pageable);
        }else if("".equals(nome)&&!"".equals(cognome)){
            return userRepository.findByCognomeContainingIgnoreCaseAndIsActive(cognome,true,pageable);
        }else if(!"".equals(nome)){
            return userRepository.findByNomeContainingIgnoreCaseAndIsActive(nome,true,pageable);
        }else{
            throw new InvalidParamsException("I parametri sono vuoti.");
        }
    }

    public List<User> findByDateBetween(String date1, String date2){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale( Locale.ITALIAN );
        LocalDate from = LocalDate.parse(date1, formatter);
        LocalDate to = LocalDate.parse(date2, formatter);

        return userRepository.findByCreatedAtDateBetween(from,to);
    }

    public Page<User> findByCitta(long id, int page, int size, String orderBy, String direction){
        Pageable pageable = PageRequest.of(page,size,Sort.by(Sort.Direction.valueOf(direction),orderBy));

        Citta citta = cittaService.findById(id);

        return userRepository.findByCittaAndIsActive(citta,true,pageable);
    }

    public User modifyById(long id, UserSignupDTO userSignupDTO){

    }

    public User findById(long id){
        return userRepository.findByIdAndIsActive(id,true).orElseThrow(()->new UserWithIdNotFoundException(id));
    }

    public User findByEmail(String email){
        return userRepository.findByEmailAndIsActive(email,true).orElseThrow(()->new UserWithEmailNotFoundException(email));
    }

    public boolean isEmailUsed(String email){
        return userRepository.findByEmail(email).isPresent();
    }
}
