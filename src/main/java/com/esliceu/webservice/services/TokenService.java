package com.esliceu.webservice.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.esliceu.webservice.models.User;
import com.esliceu.webservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {
    @Value("${token.secret}")
    String tokenSecret;

    @Value("${token.expiration}")
    int tokenExpiration;

    @Autowired
    UserRepo userRepo;
    public String newToken(String email) {
        User user  = userRepo.findByEmail(email);


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

        userMap.put("_id", user.getId());
        userMap.put("email", user.getEmail());
        userMap.put("name", user.getName());
        userMap.put("avatarUrl", "");
        userMap.put("__v", 0);
        userMap.put("id", user.getId());
        userMap.put("role", user.getRole());
        userMap.put("permissions", permissionsMap);




        return JWT.create()
                .withPayload(userMap)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpiration))
                .sign(Algorithm.HMAC512(tokenSecret.getBytes()));
    }

    public String getUser(String token) {
        return JWT.require(Algorithm.HMAC512(tokenSecret.getBytes()))
                .build()
                .verify(token)
                .getSubject();
    }
}
