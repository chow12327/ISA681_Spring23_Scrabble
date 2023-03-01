package com.isa681.scrabble;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model loginModel){
        loginModel.addAttribute("message", "Welcome to Mini Scrabble!");
        return "Login";
    }

}
