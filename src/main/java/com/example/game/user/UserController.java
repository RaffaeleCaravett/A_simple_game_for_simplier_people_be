package com.example.game.user;

import com.example.game.exceptions.InvalidParamsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/byParams")
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
}
