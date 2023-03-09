package com.esliceu.webservice.repos;

import com.esliceu.webservice.models.Reply;
import com.esliceu.webservice.models.Topic;
import com.esliceu.webservice.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

@Transactional
public interface ReplyRepo extends JpaRepository<Reply, Long> {

    @Override
    List<Reply> findAll();

    @Override
    void deleteById(Long aLong);

    List<Reply> findAllByTopic(Topic topicById);
    @Modifying
    @Query("UPDATE Reply r SET r.content=:contend, r.updatedAt =:now WHERE r.id =:idReply")
    void updateReply(int idReply, String contend, Instant now);
}
