package com.isa681.scrabble.controller;

import com.isa681.scrabble.dto.Login;
import com.isa681.scrabble.entity.Player;
import com.isa681.scrabble.entity.SignUpRequest;
import com.isa681.scrabble.service.CreateAccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@CrossOrigin(origins="https://localhost:4043")
public class CreateAccountController {

    private CreateAccountService createAccountService;

    public CreateAccountController(CreateAccountService createAccountService) {
        this.createAccountService = createAccountService;
    }

    @PostMapping("/createAccount")
    public void login(@RequestBody SignUpRequest signUpRequest){

        Player player = new Player();
        player.setFirstName(signUpRequest.firstname());
        player.setLastName(signUpRequest.lastname());
        player.setUserName(signUpRequest.username());
        player.setPassword(("{bcrypt}"+encodePassword(signUpRequest.password())).toCharArray());
        player.setEmailId(signUpRequest.emailid());
        player.setEnabled(1);
        player.setRole("ROLE_USER");

        createAccountService.createAccount(player);
    }


    private String encodePassword(String passwd) {
        BCryptPasswordEncoder bCryptPasswordEncoder =  new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(passwd);
    }

}
