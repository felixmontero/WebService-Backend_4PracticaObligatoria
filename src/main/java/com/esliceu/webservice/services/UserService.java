package com.esliceu.webservice.services;

import com.esliceu.webservice.models.User;
import com.esliceu.webservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepo userREPO;


    public void register(String name, String email, String password, String role) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(password);
        u.setRole(role);
        userREPO.save(u);
    }
}
