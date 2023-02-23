package com.esliceu.webservice.controllers;

import com.esliceu.webservice.forms.ChangePasswordForm;
import com.esliceu.webservice.forms.LoginForm;
import com.esliceu.webservice.models.User;
import com.esliceu.webservice.services.TokenService;
import com.esliceu.webservice.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;

   /* @PostMapping("/login")
    public Map<String,Object> login(@RequestBody LoginForm loginForm){
        Map<String, Object> map =  new HashMap<>();
        String email = loginForm.getEmail();
        String password = loginForm.getPassword();
        if(userService.checkLogin(email,password)){
            User user = userService.findByEmail(email);
            String token = tokenService.newToken(user.toString());
            map.put("token", token);
            System.out.println(token);
            map.put("token",token);
            return map;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Bad credentials");
    }*/
   @PostMapping("/login")
   @CrossOrigin(origins = "http://localhost:8080")
   public Map<String, Object> login(@RequestBody LoginForm loginForm, HttpServletResponse response) {

       User user = userService.findByEmail(loginForm.getEmail());
       if (user == null) {
           Map<String, Object> map = new HashMap<>();
           map.put("message", "Wrong user or password");
           response.setStatus(400);
           return map;
       } else {
           Map<String, Object> loginMap = new HashMap<>();

           String token = tokenService.newToken(loginForm.getEmail());

           loginMap.put("user", user);
           loginMap.put("token", token);
           System.out.println(token);
           return loginMap;
       }
   }

    @PostMapping("/register")
    public void register(@RequestBody User user){
        User user2 = new User();
        BeanUtils.copyProperties(user,user2);
        userService.register(user2);

    }

    @PutMapping("/profile/password")
    public void changePassword(@RequestBody ChangePasswordForm changePasswordForm, HttpServletRequest request){
        String username = (String) request.getAttribute("user");
        if(username != null || username != "") {
            String password = changePasswordForm.getNewPassword();
            String password2 = changePasswordForm.getNewPassword2();
            //BeanUtils.copyProperties(user, user2);
            //userService.changePassword(user2);

        }
    }

}
