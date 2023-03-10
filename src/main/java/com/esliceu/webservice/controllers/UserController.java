package com.esliceu.webservice.controllers;

import com.esliceu.webservice.forms.ChangePasswordForm;
import com.esliceu.webservice.forms.LoginForm;
import com.esliceu.webservice.forms.ProfileForm;
import com.esliceu.webservice.modelView.UserView;
import com.esliceu.webservice.models.Category;
import com.esliceu.webservice.models.User;
import com.esliceu.webservice.services.CategoryService;
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
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    @Autowired
    CategoryService categoryService;

   @PostMapping("/login")
   @CrossOrigin(origins = "http://localhost:8080")
   public Map<String, Object> login(@RequestBody LoginForm loginForm, HttpServletResponse response) {

       User user = userService.findByEmail(loginForm.getEmail());
       if (user == null || userService.checkLogin(loginForm.getEmail(), loginForm.getPassword()) == false){
           Map<String, Object> map = new HashMap<>();
           map.put("message", "Wrong user or password");
           response.setStatus(400);
           return map;
       } else {
           Map<String, Object> loginMap = new HashMap<>();

           String token = tokenService.newToken(loginForm.getEmail());
           UserView userView = new UserView();
           BeanUtils.copyProperties(user, userView);
           Map<String, Object> permissionsMap = new HashMap<>();

           String[] permissions = new String[]{
                   "own_topics:write",
                   "own_topics:delete",
                   "own_replies:write",
                   "own_replies:delete",
                   "categories:write",
                   "categories:delete"
           };

           permissionsMap.put("root", permissions);
           permissionsMap.put("categories",categoryService.getCategories());
           userView.setPermissions(permissionsMap);

           loginMap.put("user", userView);
           loginMap.put("token", token);
           System.out.println(token);
           return loginMap;
       }
   }

    /*@PostMapping("/register")
    public void register(@RequestBody User user){
       if(!userService.userExists(user.getEmail())){
           User user2 = new User();
           BeanUtils.copyProperties(user, user2);
           userService.register(user2);
       }else{
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User already exists");
       }
    }*/
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user){

        Map<String, String> message = new HashMap<>();
        if(!userService.userExists(user.getEmail())){
            User user2 = new User();
            BeanUtils.copyProperties(user, user2);
            userService.register(user2);
            message.put("message", "User already exists");
        }else{
            message.put("message", "User already exists");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User already exists");

        }
        return message;
    }

    @GetMapping("/getprofile")
    public Object getProfile(HttpServletRequest request, HttpServletResponse response,@RequestHeader("Authorization") String token){
        Map<String, Object> map = new HashMap<>();

        token = token.replace("Bearer ","");
        String email = tokenService.getUserEmailFromToken(token);
        User user = userService.findByEmail(email);
        Map<String, Object> userMap = new HashMap<>();
        Map<String, Object> permissionsMap = new HashMap<>();

        String[] permissions = new String[]{
                "own_topics:write",
                "own_topics:delete",
                "own_replies:write",
                "own_replies:delete",
                "categories:write",
                "categories:delete"
        };

        permissionsMap.put("root", permissions);
        List<Category> categories =  categoryService.getCategories();
        for (Category category : categories) {
            HashMap<String, Object> categoryMap = new HashMap<>();
            String[] permissions2 = new String[]{
                    "categories_replies:write",
                    "categories_replies:delete",
                    "categories_topics:write",
                    "categories_topics:delete"
            };
            categoryMap.put(category.getSlug(), permissions2);
            map.put("categories", categoryMap);
        }
        //permissionsMap.put("categories", );

        userMap.put("_id", user.getId());
        userMap.put("email", user.getEmail());
        userMap.put("name", user.getName());
        userMap.put("avatarUrl", "");
        userMap.put("__v", 0);
        userMap.put("id", user.getId());
        userMap.put("role", user.getRole());
        userMap.put("permissions", permissionsMap);
        userMap.put("iat",tokenService.getIat(token));


        return userMap;
    }

    @PutMapping("/profile")
    public Map<String,Object> updateProfile(@RequestBody ProfileForm profileForm, HttpServletRequest request, HttpServletResponse response,
                              @RequestHeader("Authorization") String token){

        token = token.replace("Bearer ","");
        String email = tokenService.getUserEmailFromToken(token);
        User user = userService.findByEmail(email);
        User user2 = userService.updateProfile(user, profileForm);

        Map<String, Object> userMap = new HashMap<>();
        Map<String, Object> permissionsMap = new HashMap<>();

        String[] permissions = new String[]{
                "own_topics:write",
                "own_topics:delete",
                "own_replies:write",
                "own_replies:delete",
                "categories:write",
                "categories:delete"
        };

        permissionsMap.put("permissions", permissions);

        userMap.put("_id", user2.getId());
        userMap.put("email", user2.getEmail());
        userMap.put("name", user2.getName());
        userMap.put("avatarUrl", "");
        userMap.put("__v", 0);
        userMap.put("id", user2.getId());
        userMap.put("role", user2.getRole());
        userMap.put("permissions", permissionsMap);
        //crate new map
        Map<String, Object> map = new HashMap<>();
        map.put("user", userMap);

        return map;

    }


    @PutMapping("/profile/password")
    public Object changePassword(@RequestBody ChangePasswordForm changePasswordForm, HttpServletRequest request, HttpServletResponse response,
                               @RequestHeader("Authorization") String token){
        token = token.replace("Bearer ","");
        String email = tokenService.getUserEmailFromToken(token);
        User user = userService.findByEmail(email);
        String newPassword = changePasswordForm.getNewPassword();
        String username = (String) request.getAttribute("user");
        userService.changePassword(user, newPassword);

        if(username != null || username != "") {

            Map<String, Object> map = new HashMap<>();
            map.put("message", "Password changed");
            return map;
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong password");
        }
    }
}
