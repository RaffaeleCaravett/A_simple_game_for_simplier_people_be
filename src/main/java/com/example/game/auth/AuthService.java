package com.example.game.auth;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

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
    @Autowired
    Cloudinary cloudinary;
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

        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            user.setImmagineProfilo(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException("Impossibile caricare l'immagine", e);
        }

        return userRepository.save(user);
    }
}
