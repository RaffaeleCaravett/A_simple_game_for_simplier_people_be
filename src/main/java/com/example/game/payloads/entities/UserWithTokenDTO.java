package com.example.game.payloads.entities;

import com.example.game.token.Token;
import com.example.game.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserWithTokenDTO{
    private User user;
    private Token token;
}
