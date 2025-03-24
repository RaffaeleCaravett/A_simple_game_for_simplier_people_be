package com.example.game.auth;

import com.example.game.citta.CittaService;
import com.example.game.email.EmailService;
import com.example.game.enums.Role;
import com.example.game.exceptions.*;
import com.example.game.jwt.JWTTools;
import com.example.game.payloads.entities.UserLoginDTO;
import com.example.game.payloads.entities.UserSignupDTO;
import com.example.game.payloads.entities.UserWithTokenDTO;
import com.example.game.token.Token;
import com.example.game.user.User;
import com.example.game.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    UserService userService;
    @Autowired
    CittaService cittaService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EmailService emailService;
    @Autowired
    JWTTools jwtTools;

    public User save(UserSignupDTO userSignupDTO, MultipartFile multipartFile) {
        User user = new User();
        if (userService.isEmailUsed(userSignupDTO.email())) {
            throw new EmailAlreadyInUseException(userSignupDTO.email());
        }
        user.setActive(true);
        user.setNome(userSignupDTO.nome());
        user.setCognome(userSignupDTO.cognome());
        user.setFullName(user.getNome(),user.getCognome());
        user.setCitta(cittaService.findById(userSignupDTO.cittaId()));
        user.setEmail(userSignupDTO.email());
        user.setPassword(passwordEncoder.encode(userSignupDTO.password()));
        user.setCreatedAt(LocalDate.now().toString());
        user.setRole(Role.User);
        user.setValidated(false);
        userService.setProfileImage(user, multipartFile);

        return userService.save(user);
    }

    public UserWithTokenDTO login(UserLoginDTO userLoginDTO) {
        User user = userService.findByEmail(userLoginDTO.email());

        if(!user.isValidated()&&passwordEncoder.matches(userLoginDTO.password(), user.getPassword())){
            userService.askForCode(user.getEmail(), true);
            throw new AccessDeniedException("Abbiamo inviato un codice alla mail da te indicata. Inseriscilo qui sotto.");
        }else if (user.isValidated()&&passwordEncoder.matches(userLoginDTO.password(), user.getPassword())) {
            try {
                Token token = jwtTools.createTokens(user);
                return new UserWithTokenDTO(user, token);
            } catch (Exception e) {
                throw new BadRequestException(e.getMessage());
            }
        }else if(!passwordEncoder.matches(userLoginDTO.password(), user.getPassword())){
            throw new AccessDeniedException("La password è errata.");
        }else{
            throw new AccessDeniedException("E' successo qualcosa di inaspettato. Contatta l'assistenza dalla pagina dedicata.");
        }
    }

    public User verifyToken(String token) {
        return jwtTools.verifyToken(token);
    }

    public Token verifyRefreshToken(String token) {
        return jwtTools.verifyRefreshToken(token);
    }

    public Integer getAllUsersCount() {
        return userService.findAll().size();
    }

    public User changePasswordByCode(String email, String code, String newPassword) {
        User user = userService.findByEmail(email);

        if (user.getChangePasswordCode().equals(code)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            return userService.save(user);
        } else {
            throw new CodeMismatchException(code);
        }
    }

    public boolean resetPassword(String password, String oldPassword, long id) {
        User user = userService.findById(id);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new PasswordMismatchException("La vecchia password non coincide con quella che abbiamo noi in database");
        }
        try {
            user.setPassword(passwordEncoder.encode(password));
            userService.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
