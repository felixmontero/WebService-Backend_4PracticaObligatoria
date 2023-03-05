package com.esliceu.webservice.repos;

import com.esliceu.webservice.models.Category;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
@Transactional
public interface CategoryRepo extends JpaRepository<Category, Long> {

    List<Category> findAll();
    void delete(Category category);
}
