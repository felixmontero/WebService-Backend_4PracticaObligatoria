package com.esliceu.webservice.services;

import com.esliceu.webservice.models.Reply;
import com.esliceu.webservice.models.User;
import com.esliceu.webservice.repos.ReplyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReplyService {
    @Autowired
    ReplyRepo replyRepo;
    @Autowired TopicService topicService;
    public Reply createReply(int id, User user, String content) {
        Reply reply= new Reply();
        reply.setId(generateId());
        reply.setUser(user);
        reply.setContent(content);
        reply.setCreatedAt(Instant.now());
        reply.setUpdatedAt(Instant.now());
        reply.setTopic(topicService.getTopicById(id));

        replyRepo.save(reply);

        return reply;
    }

    public int generateId(){

        int id= replyRepo.findAll().stream().mapToInt(Reply::getId).max().orElse(0);
        return id+1;


    }

    public List<Reply> getRepliesByTopicId(int id) {
        return replyRepo.findAllByTopic(topicService.getTopicById(id));
    }

    public void deleteReply(int id) {
        replyRepo.deleteById((long) id);

    }

    public void updateReply(int idReply, String contend) {
        replyRepo.updateReply(idReply,contend,Instant.now());
    }

    public List<Object> getReplyListMap(List<Reply> replyList, long topicId) {
        List<Object> repliesMap = new ArrayList<>();
        for (Reply reply : replyList) {
            Map<String, Object> replyMap = new HashMap<>();
            replyMap.put("content", reply.getContent());
            replyMap.put("createdAt", reply.getCreatedAt());
            replyMap.put("topic", topicId);
            replyMap.put("updatedAt", reply.getUpdatedAt());
            replyMap.put("_id", reply.getId());

            User user = reply.getUser();
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

            replyMap.put("user", userMap);
            repliesMap.add(replyMap);
        }
        return repliesMap;
    }

    public Reply findById(int idReply) {
        return replyRepo.findById((long) idReply).orElse(null);
    }
}
