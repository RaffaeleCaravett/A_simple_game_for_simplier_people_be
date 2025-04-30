package com.example.game.auth;

import com.example.game.citta.Citta;
import com.example.game.citta.CittaService;
import com.example.game.exceptions.BadRequestException;
import com.example.game.gioco.Gioco;
import com.example.game.gioco.GiocoRepository;
import com.example.game.payloads.entities.GoogleUser;
import com.example.game.payloads.entities.UserLoginDTO;
import com.example.game.payloads.entities.UserSignupDTO;
import com.example.game.payloads.entities.UserWithTokenDTO;
import com.example.game.token.Token;
import com.example.game.user.User;
import com.example.game.user.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final GiocoRepository giocoRepository;
    @Autowired
    AuthService authService;
    @Autowired
    CittaService cittaService;
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserWithTokenDTO> login(@RequestBody @Validated UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }

        return ResponseEntity.ok(authService.login(userLoginDTO));
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
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable long id){
        return userService.findById(id);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<Integer> getAllUsersCount(){
        return ResponseEntity.ok(authService.getAllUsersCount());
    }

    @GetMapping("/requestCode/{email}/{validation}")
    public boolean askCodeByEmail(@PathVariable String email,@PathVariable boolean validation){
       User user = userService.findByEmail(email);
       return userService.askForCode(user.getEmail(),validation);
    }

    @GetMapping("/verifyPasswordCode/{email}/{code}/{validation}")
    public boolean verifyCodeByEmail(@PathVariable String email,@PathVariable String code, @PathVariable boolean validation){
        return userService.verifyChangePasswordCode(email,code,validation);
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

    @GetMapping("/gioco")
    public List<Gioco> getAll(){
        return giocoRepository.findAll();
    }

    @PutMapping("/gioco/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public Gioco updateGiocoImage(@RequestParam(name = "gioco_image") MultipartFile multipartFile,@PathVariable long id){
        var gioco = giocoRepository.findById(id).orElseThrow();
        try {
            byte[] fileBytes = multipartFile.getBytes();
            gioco.setImage(fileBytes);
            return giocoRepository.save(gioco);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return gioco;
        }
    }
    @GetMapping("/clear/{email}")
    public void clear(@PathVariable String email){
        userService.clearCode(email);
    }

    @PostMapping("/googleUser")
    public User signupGoogleUser(@RequestBody @Validated GoogleUser googleUser, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw  new BadRequestException(bindingResult.getAllErrors());
        }
        return authService.signupGoogleUser(googleUser);
    }
}
