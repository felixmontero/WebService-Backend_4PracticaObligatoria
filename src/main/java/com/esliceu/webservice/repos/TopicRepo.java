package com.esliceu.webservice.repos;

import com.esliceu.webservice.models.Category;
import com.esliceu.webservice.models.Topic;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Transactional
public interface TopicRepo extends JpaRepository<Topic, Long> {

    List<Topic> findAll();
    void delete(Topic topic);
    List<Topic> findAllByCategory(Category category);
    Topic findById(long id);
}
