package com.example.game.payloads.entities;

public record SocketDTO (
     MessageDTO messageDTO,
     MoveDTO moveDTO,
     GameConnectionDTO gameConnectionDTO,
     ConnectionDTO connectionDTO
){
}
