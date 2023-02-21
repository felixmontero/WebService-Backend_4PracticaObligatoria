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
}
