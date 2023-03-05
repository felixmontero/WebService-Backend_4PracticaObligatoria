package com.esliceu.webservice.services;

import com.esliceu.webservice.forms.CreateCategoryForm;
import com.esliceu.webservice.models.Category;
import com.esliceu.webservice.repos.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepo categoryRepo;

    public List<Category> getCategories() {
        return categoryRepo.findAll();
    }

    public void createCategory(CreateCategoryForm category) {
        Category newCategory = new Category();
        newCategory.setTitle(category.getTitle());
        newCategory.setDescription(category.getDescription());
        categoryRepo.save(newCategory);

    }
}
