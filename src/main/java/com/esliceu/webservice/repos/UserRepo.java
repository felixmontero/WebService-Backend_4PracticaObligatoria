package com.esliceu.webservice.repos;

import com.esliceu.webservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User save(User user);

    boolean existsByEmailAndPassword(String email, String password);

    User findByEmail(String email);
}
