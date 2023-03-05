package com.esliceu.webservice.services;

import com.esliceu.webservice.forms.CreateCategoryForm;
import com.esliceu.webservice.models.Category;
import com.esliceu.webservice.repos.CategoryRepo;
import org.springframework.beans.BeanUtils;
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

    public Category createCategory(CreateCategoryForm category) {
        Category newCategory = new Category();
        BeanUtils.copyProperties(category,newCategory);
        newCategory.setId(generateId());
        System.out.println(newCategory.toString());
        categoryRepo.save(newCategory);
        return newCategory;
    }
    public int generateId(){
        if(categoryRepo.findAll().size()==0){
            return 0;
        }else {
           return categoryRepo.findAll().size();
        }

    }

    public void updateCategory(String slug, String title, String description) {
        Category category = categoryRepo.findAll().stream().filter(c -> c.getSlug().equals(slug)).findFirst().orElse(null);
        category.setTitle(title);
        category.setDescription(description);
        categoryRepo.save(category);
    }

    public void removeCategory(String slug) {
        Category category = categoryRepo.findAll().stream().filter(c -> c.getSlug().equals(slug)).findFirst().orElse(null);
        categoryRepo.delete(category);
    }
}
