package com.esliceu.webservice.services;

import com.esliceu.webservice.models.Category;
import com.esliceu.webservice.models.Topic;
import com.esliceu.webservice.models.User;
import com.esliceu.webservice.repos.TopicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TopicService {
    @Autowired
    TopicRepo topicRepo;
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;

    public List<Topic> getTopicsByCategory(Category category) {

        return topicRepo.findAllByCategory(category);
    }

    public Topic getTopicById(String id) {
        return topicRepo.findById(Long.parseLong(id));
    }

    public Topic createTopic(String title, String content, String category, String email) {
        User user = userService.findByEmail(email);
        Category category1 = categoryService.getCategoryBySlug(category);
        Topic topic = new Topic();
        topic.setId((int) (topicRepo.count() + 1));
        topic.setTitle(title);
        topic.setContent(content);
        topic.setCategory(category1);
        topic.setUser(user);
        topic.setCreatedAt(Instant.now());
        topic.setUpdatedAt(Instant.now());
        return topicRepo.save(topic);
    }


    public Topic updateTopic(String id, String title, String content, String category, String email) {
        User user = userService.findByEmail(email);
        Category category1 = categoryService.getCategoryBySlug(category);
        Topic topic = topicRepo.findById(Long.parseLong(id));
        topic.setTitle(title);
        topic.setContent(content);
        topic.setCategory(category1);
        topic.setUser(user);
        topic.setUpdatedAt(Instant.now());
        return topicRepo.save(topic);
    }


    public Topic deleteTopic(String id, String email) {
        User user = userService.findByEmail(email);
        Topic topic = topicRepo.findById(Long.parseLong(id));
        topicRepo.delete(topic);
        return topic;
    }
}
