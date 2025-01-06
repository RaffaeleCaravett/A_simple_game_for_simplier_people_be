package com.example.game.user;

import com.example.game.exceptions.InvalidParamsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;


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
}
