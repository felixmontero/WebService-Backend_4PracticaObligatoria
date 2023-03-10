package com.esliceu.webservice.controllers;

import com.esliceu.webservice.forms.CreateTopicForm;
import com.esliceu.webservice.modelView.UserView;
import com.esliceu.webservice.models.Category;
import com.esliceu.webservice.models.Reply;
import com.esliceu.webservice.models.Topic;
import com.esliceu.webservice.models.User;
import com.esliceu.webservice.services.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class TopicController {

    @Autowired
    TopicService topicService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    TokenService tokenService;
    @Autowired
    UserService userService;
    @Autowired
    ReplyService replyService;
    @GetMapping("/categories/{slug}/topics")
    public Map<String,Object> getTopicsByCategory(@PathVariable String slug, HttpServletResponse response, @RequestHeader("Authorization") String token){
        Map<String, Object> map = new HashMap<>();
        Category category = categoryService.getCategoryBySlug(slug);
        if(category == null){
            response.setStatus(404);
            return null;
        }

        List<Topic> topics = topicService.getTopicsByCategory(category);
        for(Topic topic : topics){
            HashMap<String, Object> userMap = new HashMap<>();
            HashMap<String, Object> topicMap = new HashMap<>();
            User user = topic.getUser();
            UserView user2 = new UserView();
            BeanUtils.copyProperties(user, user2);

            userMap.put("id", user.getId());
            userMap.put("name", user.getName());
            userMap.put("email", user.getEmail());
            userMap.put("avatar", "");
            userMap.put("role", user.getRole());

            topicMap.put("_id", topic.getId());
            topicMap.put("title", topic.getTitle());
            topicMap.put("content", topic.getContent());
            topicMap.put("category", category.getId());
            topicMap.put("views", 0);

            topicMap.put("user", userMap);
            topicMap.put("createdAt", topic.getCreatedAt());
            topicMap.put("updatedAt", topic.getUpdatedAt());
            topicMap.put("__v", 0);
            topicMap.put("replies",null);
            topicMap.put("numberOfReplies", 0);
            topicMap.put("id", topic.getId());

            map.put(""+topic.getId(), topicMap);

        }



            return map;


    }

    @GetMapping("/topics/{id}")
    public Map<String,Object> getTopicById(@PathVariable int id, HttpServletResponse response, @RequestHeader("Authorization") String token){

        Topic topic = topicService.getTopicById(id);
        if(topic == null){
            response.setStatus(404);
            return null;
        }
        List<Reply> replies = replyService.getRepliesByTopicId(id);
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> categoryMap = new HashMap<>();
        HashMap<String, Object> userMap = new HashMap<>();
        User user = topic.getUser();
        Category category = topic.getCategory();
        categoryMap.put("moderators", new String[]{});
        categoryMap.put("_id", category.getId());
        categoryMap.put("title", category.getTitle());
        categoryMap.put("description", category.getDescription());
        categoryMap.put("slug", category.getSlug());
        categoryMap.put("color", category.getColor());
        categoryMap.put("__v", 0);
        userMap.put("_id", user.getId());
        userMap.put("id", user.getId());
        userMap.put("name", user.getName());
        userMap.put("email", user.getEmail());
        userMap.put("avatarUrl", "");
        userMap.put("role", user.getRole());
        userMap.put("__v", 0);
        // REPLIES
        for (Reply reply : replies) {
            HashMap<String, Object> replyMap = new HashMap<>();
            HashMap<String, Object> userReplyMap = new HashMap<>();
            User userReply = reply.getUser();
            userReplyMap.put("_id", userReply.getId());
            userReplyMap.put("id", userReply.getId());
            userReplyMap.put("name", userReply.getName());
            userReplyMap.put("email", userReply.getEmail());
            userReplyMap.put("avatarUrl", "");
            userReplyMap.put("role", userReply.getRole());
            userReplyMap.put("__v", 0);
            replyMap.put("_id", reply.getId());
            replyMap.put("id", reply.getId());
            replyMap.put("content", reply.getContent());
            replyMap.put("user", userReplyMap);
            replyMap.put("topic", topic.getId());
            replyMap.put("createdAt", reply.getCreatedAt());
            replyMap.put("updatedAt", reply.getUpdatedAt());
            replyMap.put("__v", 0);


        }
       // map.put("reply"+reply.getId(), replyMap);




        map.put("replies", replyService.getReplyListMap(replies, topic.getId()));
        map.put("_id", topic.getId());
        map.put("id", topic.getId());
        map.put("title", topic.getTitle());
        map.put("content", topic.getContent());
        map.put("category", categoryMap);
        map.put("user", userMap);
        map.put("numberOfReplies", null);
        map.put("createdAt", topic.getCreatedAt());
        map.put("updatedAt", topic.getUpdatedAt());
        map.put("__v", 0);


        //A??adir replies

        return map;
    }

    @PostMapping("/topics")
    public Map<String,Object> createTopic(@RequestBody CreateTopicForm createTopicForm, HttpServletResponse response, @RequestHeader("Authorization") String token){
        Map<String, Object> map = new HashMap<>();
        token = token.replace("Bearer ","");
        String email = tokenService.getUserEmailFromToken(token);
        Topic topic = topicService.createTopic(createTopicForm.getTitle(), createTopicForm.getContent(), createTopicForm.getCategory(), email);
        User user = userService.findByEmail(email);

        if(topic == null){
            response.setStatus(404);
            return null;
        }
        map.put("id", topic.getId());
        map.put("title", topic.getTitle());
        map.put("content", topic.getContent());
        map.put("category", topic.getCategory().getId());
        map.put("user", user.getId());
        map.put("createdAt", topic.getCreatedAt());
        map.put("updatedAt", topic.getUpdatedAt());
        map.put("__v", 0);
        map.put("replies",null);
        map.put("numberOfReplies", 0);
        map.put("views", 0);
        map.put("_id", topic.getId());
        return map;
    }

    @PutMapping("/topics/{id}")
    public Map<String,Object> updateTopic(@PathVariable String id, @RequestBody CreateTopicForm createTopicForm, HttpServletResponse response, @RequestHeader("Authorization") String token){
        Map<String, Object> map = new HashMap<>();
        token = token.replace("Bearer ","");
        String email = tokenService.getUserEmailFromToken(token);
        Topic topic = topicService.updateTopic(id, createTopicForm.getTitle(), createTopicForm.getContent(), createTopicForm.getCategory(), email);
        User user = userService.findByEmail(email);
        List<Reply> replies = replyService.getRepliesByTopicId(Integer.parseInt(id));
        if(topic == null){
            response.setStatus(404);
            return null;
        }
        map.put("id", topic.getId());
        map.put("title", topic.getTitle());
        map.put("content", topic.getContent());
        map.put("category", topic.getCategory().getId());
        map.put("user", user.getId());
        map.put("createdAt", topic.getCreatedAt());
        map.put("updatedAt", topic.getUpdatedAt());
        map.put("__v", 0);
        map.put("replies",replies);
        map.put("numberOfReplies", 0);
        map.put("views", 0);
        map.put("_id", topic.getId());
        return map;
    }

    @DeleteMapping("/topics/{id}")
    public Map<String,Object> deleteTopic(@PathVariable String id, HttpServletResponse response, @RequestHeader("Authorization") String token){
        Map<String, Object> map = new HashMap<>();
        token = token.replace("Bearer ","");
        String email = tokenService.getUserEmailFromToken(token);

        User user = userService.findByEmail(email);

        List<Reply> replies = replyService.getRepliesByTopicId(Integer.parseInt(id));
        for(Reply reply : replies){
                replyService.deleteReply(reply.getId());
        }
        Topic topic = topicService.deleteTopic(Integer.parseInt(id), email);

        if(topic == null){
            response.setStatus(404);
            return null;
        }
        map.put("id", topic.getId());
        map.put("title", topic.getTitle());
        map.put("content", topic.getContent());
        map.put("category", topic.getCategory().getId());
        map.put("user", user.getId());
        map.put("createdAt", topic.getCreatedAt());
        map.put("updatedAt", topic.getUpdatedAt());
        map.put("__v", 0);
        map.put("replies",null);
        map.put("numberOfReplies", 0);
        map.put("views", 0);
        map.put("_id", topic.getId());
        return map;
    }
}
