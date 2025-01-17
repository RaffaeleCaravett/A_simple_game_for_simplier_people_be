package com.example.game.payloads.entities;

import com.example.game.token.Token;
import com.example.game.user.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserWithTokenDTO{
    private User user;
    private Token token;
}
