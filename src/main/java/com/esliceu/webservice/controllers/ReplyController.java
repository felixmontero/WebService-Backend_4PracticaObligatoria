package com.esliceu.webservice.controllers;

import com.esliceu.webservice.forms.ReplyForm;
import com.esliceu.webservice.models.Reply;
import com.esliceu.webservice.models.User;
import com.esliceu.webservice.services.ReplyService;
import com.esliceu.webservice.services.TokenService;
import com.esliceu.webservice.services.TopicService;
import com.esliceu.webservice.services.UserService;
import com.esliceu.webservice.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class ReplyController {

    @Autowired
    ReplyService replyService;
    @Autowired
    TopicService topicService;
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;

    @PostMapping("/topics/{id}/replies")
    public Map<String,Object> createReply(@PathVariable int id, @RequestHeader("Authorization") String token, @RequestBody ReplyForm replyForm) {

        String email = tokenService.getUserEmailFromToken(token.replace("Bearer ", ""));
        User user = userService.findByEmail(email);
        Reply reply1 = null;
        if(user != null ) {
            String contend = replyForm.getContent().replace("script", "");
            reply1 = replyService.createReply(id, user, contend);
        }
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("name", user.getName());
        userMap.put("email", user.getEmail());
        userMap.put("avatarUrl", "");
        userMap.put("role", user.getRole());
        userMap.put("__v", 0);
        userMap.put("_id", user.getId());

        String[] permissions = new String[]{
                "own_topics:write",
                "own_topics:delete",
                "own_replies:write",
                "own_replies:delete",
                "categories:write",
                "categories:delete"
        };
        userMap.put("permissions", permissions);
        map.put("user", userMap);


        map.put("content", replyForm.getContent());
        //parsear el id a string

        map.put("topic", ""+id);
        map.put("createdAt", Instant.now());
        map.put("updatedAt", Instant.now());
        map.put("__v",0);
        map.put("_id",""+reply1.getId());

        return map;
    }

    @PutMapping("/topics/{id}/replies/{idReply}")
    public Map<String, Object> updateReply(@PathVariable("id") int id, @PathVariable("idReply") int idReply, @RequestHeader("Authorization") String token, @RequestBody ReplyForm replyForm) {
        String email = tokenService.getUserEmailFromToken(token.replace("Bearer ", ""));
        User user = userService.findByEmail(email);

        if(user != null ) {
            String contend = replyForm.getContent().replace("script", "");
            replyService.updateReply(idReply, contend);
        }
        HashMap<String, Object> map = new HashMap<>();
        Reply reply = replyService.findById(idReply);
        map.put("user", user);
        map.put("content", replyForm.getContent());
        //parsear el id a string

        map.put("topic", id);
        map.put("createdAt", Instant.now());
        map.put("updatedAt", Instant.now());
        map.put("__v",0);
        //map.put("__id",reply.getId());

        return map;
    }

    @DeleteMapping("/topics/{id}/replies/{idReply}")
    public boolean deleteReply(@PathVariable("id") int id, @PathVariable("idReply") int idReply) {
        replyService.deleteReply(idReply);

        return true;
    }
}