package com.example.game.user;

import com.example.game.exceptions.BadRequestException;
import com.example.game.exceptions.InvalidParamsException;
import com.example.game.payloads.entities.UserSignupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    /*GET*/

    @GetMapping("/byParams")
    @PreAuthorize("hasAuthority('User')")
    public Page<User> findByParamsAndIsActive(@RequestParam(defaultValue = "") String nome,
                                              @RequestParam(defaultValue = "") String cognome,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id") String orderBy,
                                              @RequestParam(defaultValue = "ASC") String direction) {
        if((nome + cognome).isEmpty()){
            throw new InvalidParamsException("I parametri che hai passato sono vuoti");
        }else{
            return userService.findByParamsAndIsActive(nome,cognome,page,size,orderBy,direction);
        }
    }

    @GetMapping("/byDates")
    @PreAuthorize("hasAuthority('User')")
    public Page<User> findByDateBetween(@RequestParam(defaultValue = "") String date1,
                                        @RequestParam(defaultValue = "") String date2,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "id") String orderBy,
                                        @RequestParam(defaultValue = "ASC") String direction){

        if ("".equals(date1)||"".equals(date2)){
            throw new InvalidParamsException("Le date che stai passando non sono valide.");
        }
        try {
            return userService.findByDateBetween(date1,date2,page,size,orderBy,direction);
        }catch (Exception e){
            throw new InvalidParamsException("Le date che stai passando non sono valide.");
        }
    }

    @GetMapping("/byCitta/{id}")
    @PreAuthorize("hasAuthority('User')")
    public Page<User> findByCitta(@PathVariable long id,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String orderBy,
                                  @RequestParam(defaultValue = "ASC") String direction){
        return userService.findByCitta(id,page,size,orderBy,direction);
    }

    @GetMapping("/restoreById")
    @PreAuthorize("hasAuthority('User')")
    public User restoreById(@AuthenticationPrincipal User user){
        return userService.restoreById(user.getId());
    }

    @GetMapping("/askForCode/{email}")
    public boolean askForCode(@PathVariable String email){
        return userService.askForCode(email);
    }

    /*PUT*/

    @PutMapping("")
    @PreAuthorize("hasAuthority('User')")
    public User findByCitta(@AuthenticationPrincipal User user, @RequestBody @Validated UserSignupDTO userSignupDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return userService.modifyById(user.getId(),userSignupDTO);
    }
    @PutMapping("/profileImage")
    @PreAuthorize("hasAuthority('User')")
    public String modifyProfileImage(@AuthenticationPrincipal User user, @RequestPart(name = "profile_image") MultipartFile multipartFile){
        return userService.setProfileImage(user,multipartFile);
    }

    /*DELETE*/

    @DeleteMapping("")
    @PreAuthorize("hasAuthority('User')")
    public boolean deleteById(@AuthenticationPrincipal User user){
        return userService.deleteById(user.getId());
    }
    @DeleteMapping("/permanently")
    @PreAuthorize("hasAuthority('User')")
    public boolean deleteByIdPermanently(@AuthenticationPrincipal User user){
        return userService.permanentlyDeleteUser(user.getId());
    }
}
