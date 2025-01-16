package com.example.game.auth;

import com.example.game.citta.Citta;
import com.example.game.citta.CittaService;
import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.UserLoginDTO;
import com.example.game.payloads.entities.UserSignupDTO;
import com.example.game.payloads.entities.UserWithTokenDTO;
import com.example.game.token.Token;
import com.example.game.user.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;
    @Autowired
    CittaService cittaService;


    @PostMapping("/login")
    public UserWithTokenDTO login(@RequestBody @Validated UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }

        return authService.login(userLoginDTO);
    }

    @PostMapping("/signup")
    public User login(@RequestPart(name = "user_signup") @Validated UserSignupDTO userLoginDTO, @RequestPart(name = "profile_image") MultipartFile multipartFile, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }

        return authService.save(userLoginDTO,multipartFile);
    }

    @GetMapping("/verifyToken")
    public User verifyToken(@RequestParam(defaultValue = "") String token){
        return authService.verifyToken(token);
    }

    @GetMapping("/verifyRefreshToken")
    public Token verifyRefreshToken(@RequestParam(defaultValue = "") String refreshToken){
        return authService.verifyRefreshToken(refreshToken);
    }

    @GetMapping("/cities")
    public List<Citta> getCities(){
        return cittaService.findAll();
    }

    @GetMapping("/allUsers")
    public ResponseEntity<Integer> getAllUsersCount(){
        return ResponseEntity.ok(authService.getAllUsersCount());
    }

}
