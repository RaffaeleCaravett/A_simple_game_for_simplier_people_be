package com.example.game.auth;

import com.example.game.citta.Citta;
import com.example.game.citta.CittaService;
import com.example.game.exceptions.BadRequestException;
import com.example.game.payloads.entities.UserLoginDTO;
import com.example.game.payloads.entities.UserSignupDTO;
import com.example.game.payloads.entities.UserWithTokenDTO;
import com.example.game.token.Token;
import com.example.game.user.User;
import com.example.game.user.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @Autowired
    UserService userService;

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

    @GetMapping("/requestCode/{email}")
    public boolean askCodeByEmail(@PathVariable String email){
       User user = userService.findByEmail(email);
       return userService.askForCode(user.getEmail());
    }

    @GetMapping("/verifyPasswordCode/{email}/{code}")
    public boolean verifyCodeByEmail(@PathVariable String email,@PathVariable String code){
        return userService.verifyChangePasswordCode(email,code);
    }

    @GetMapping("/resetPasswordByCode/{newPassword}/{email}/{code}")
    public User sendCodeByEmail(@PathVariable String newPassword,@PathVariable String email,@PathVariable String code){
        return authService.changePasswordByCode(email,code,newPassword);
    }

    @GetMapping("/changePassword/{oldPassword}/{newPassword}")
    public boolean changePassword(@PathVariable String oldPassword,
                                  @PathVariable String newPassword,
                                  @AuthenticationPrincipal User user){
        return authService.resetPassword(newPassword,oldPassword,user.getId());
    }
}
