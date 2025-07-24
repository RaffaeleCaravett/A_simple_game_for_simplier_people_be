package com.example.game.socket.connection;

import com.example.game.user.User;
import com.example.game.user.UserRepository;
import com.example.game.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/connection")
public class ConnectionController {

    @Autowired
    private UserRepository userDao;

    @Autowired
    private SimpMessagingTemplate template;

    public void convertAndSend (User user){
        template.convertAndSend("/channel/login", user);

    }

    public void logout(User user) {
        template.convertAndSend("/channel/logout", user);
    }

    @GetMapping("/listUsers/{page}")
    public Page<User> findConnectedUsers(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page,30);
        return userDao.findAll(Specification.where(UserRepository.isConnected(true)),pageable);
    }


}
