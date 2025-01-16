package com.example.game.auth;

import com.example.game.citta.Citta;
import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.UserLoginDTO;
import com.example.game.payloads.entities.UserWithTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;


    public UserWithTokenDTO login(@RequestBody @Validated UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }

        return authService.login(userLoginDTO);
    }
}
