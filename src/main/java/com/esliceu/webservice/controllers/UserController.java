package com.esliceu.webservice.controllers;

import com.esliceu.webservice.models.User;
import com.esliceu.webservice.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @CrossOrigin
    @PostMapping("/login")
    public void login(@RequestParam String user, @RequestParam String password){

    }

    @PostMapping("/register")
    @CrossOrigin("http://localhost:3000")
    public void register(@RequestBody User user){
        User user2 = new User();
        BeanUtils.copyProperties(user,user2);
        userService.register(user2);

    }
}
