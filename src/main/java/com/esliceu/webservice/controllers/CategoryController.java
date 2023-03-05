package com.esliceu.webservice.controllers;

import com.esliceu.webservice.forms.CreateCategoryForm;
import com.esliceu.webservice.models.Category;
import com.esliceu.webservice.services.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> getCategories(HttpServletResponse response){
       List<Category> categories = new ArrayList<>();
       categories = categoryService.getCategories();
       //recorrer la lista de categorias y crear el objeto y añadirlo a una arraylist


       return categories;
    }

    @PostMapping("/categories")
    public void addCategory(HttpServletResponse response, @RequestBody CreateCategoryForm createCategoryForm){

        categoryService.createCategory(createCategoryForm);

    }
}
