package com.esliceu.webservice.services;

import com.esliceu.webservice.models.User;
import com.esliceu.webservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepo userREPO;


    public void register(User user) {

        userREPO.save(user);
    }

    public boolean checkLogin(String email, String password) {
        return userREPO.existsByEmailAndPassword(email,password);
    }

    public User findByEmail(String email) {
        return userREPO.findByEmail(email);
    }

    public void changePassword(User user2) {

    }
}
