package com.example.game.auth;

import com.example.game.citta.CittaService;
import com.example.game.enums.Role;
import com.example.game.exceptions.BadRequestException;
import com.example.game.exceptions.EmailAlreadyInUseException;
import com.example.game.payloads.entities.UserSignupDTO;
import com.example.game.user.User;
import com.example.game.user.UserRepository;
import com.example.game.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    CittaService cittaService;
    @Autowired
    PasswordEncoder passwordEncoder;
    public User save(@RequestPart(name = "user") @Validated UserSignupDTO userSignupDTO,
                     @RequestPart(name = "profile_image") MultipartFile multipartFile,
                     BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        if(userService.isEmailUsed(userSignupDTO.email())){
            throw new EmailAlreadyInUseException(userSignupDTO.email());
        }
        User user = new User();
        user.setActive(true);
        user.setNome(userSignupDTO.nome());
        user.setCognome(userSignupDTO.cognome());
        user.setCitta(cittaService.findById(userSignupDTO.cittaId()));
        user.setEmail(userSignupDTO.email());
        user.setPassword(passwordEncoder.encode(userSignupDTO.password()));
        user.setCreatedAt(LocalDate.now().toString());
        user.setRole(Role.User);

        return userRepository.save(user);
    }
}
