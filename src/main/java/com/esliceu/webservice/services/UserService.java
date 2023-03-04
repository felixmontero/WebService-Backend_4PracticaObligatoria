package com.esliceu.webservice.services;

import com.esliceu.webservice.forms.ChangePasswordForm;
import com.esliceu.webservice.forms.ProfileForm;
import com.esliceu.webservice.models.User;
import com.esliceu.webservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    public void changePassword(User user2, String newPassword) {
        user2.setPassword(newPassword);
        userREPO.save(user2);
    }

    public Map<String, Object> convertUserToJson(User user) {

        String[] permissions = new String[]{
                "own_topics:write",
                "own_topics:delete",
                "own_replies:write",
                "own_replies:delete",
                "categories:write",
                "categories:delete"
        };

        Map<String, Object> permissionsMap = new HashMap<>();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("_id", user.getId());
        userMap.put("email", user.getEmail());
        userMap.put("name", user.getName());
        userMap.put("avatarUrl", "");
        userMap.put("__v", 0);
        userMap.put("id", user.getId());
        userMap.put("role", user.getRole());
        userMap.put("permissions", permissionsMap);
        return userMap;
    }

    public boolean userExists(String email) {
        return userREPO.existsByEmail(email);
    }

    public User updateProfile(User user, ProfileForm profileForm) {
        user.setName(profileForm.getName());
        user.setEmail(profileForm.getEmail());
        userREPO.save(user);
        return user;

    }
}
