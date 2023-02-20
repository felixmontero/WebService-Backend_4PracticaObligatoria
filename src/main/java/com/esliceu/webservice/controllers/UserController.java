package com.esliceu.webservice.controllers;

import com.esliceu.webservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public void login(@RequestParam String user, @RequestParam String password){

    }

    @PostMapping("/register")
    public void register(@RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam String role){
        userService.register(name, email, password, role);
    }
}
