package com.esliceu.webservice.controllers;

import com.esliceu.webservice.forms.CreateCategoryForm;
import com.esliceu.webservice.models.Category;
import com.esliceu.webservice.models.Reply;
import com.esliceu.webservice.models.Topic;
import com.esliceu.webservice.services.CategoryService;
import com.esliceu.webservice.services.ReplyService;
import com.esliceu.webservice.services.TokenService;
import com.esliceu.webservice.services.TopicService;
import com.esliceu.webservice.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.transform.ClassEmitterTransformer;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@RestController
@CrossOrigin
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    TopicService topicService;
    @Autowired
    ReplyService replyService;
    @GetMapping("/categories")
    public List<Category> getCategories(HttpServletResponse response){
       List<Category> categories = new ArrayList<>();
       categories = categoryService.getCategories();
       //recorrer la lista de categorias y crear el objeto y añadirlo a una arraylist

       return categories;
    }

    @PostMapping("/categories")
    public Map<String,Object> addCategory(HttpServletResponse response, @RequestBody CreateCategoryForm createCategoryForm){
        createCategoryForm.setColor(Utils.randomColor());
        createCategoryForm.setSlug(createCategoryForm.getTitle().replace(" ",""));

        //modificar para devolver valores
        Category category = categoryService.createCategory(createCategoryForm);
        Map<String,Object> map2 = new HashMap<>();

        map2.put("_id",category.getId());
        map2.put("title",category.getTitle());
        map2.put("description",category.getDescription());
        map2.put("slug",category.getSlug());
        map2.put("color",category.getColor());
        map2.put("__v",0);
        map2.put("moderators",new String[]{});

        return map2;

    }

    @GetMapping("/categories/{slug}")
    public Map<String, Object> CategorySlug(HttpServletResponse response, @PathVariable String slug){

        Category category = new Category();
        category = categoryService.getCategories().stream().filter(c -> c.getSlug().equals(slug)).findFirst().orElse(null);

        if(category == null){
            response.setStatus(404);
            return null;
        }
        Map<String,Object> categoryMap = new HashMap<>();

        categoryMap.put("_id",category.getId());
        categoryMap.put("title",category.getTitle());
        categoryMap.put("description",category.getDescription());
        categoryMap.put("slug",category.getSlug());
        categoryMap.put("color",category.getColor());
        categoryMap.put("__v",0);
        categoryMap.put("moderators",new String[]{});

        return categoryMap;

    }

    //update
    @PutMapping("/categories/{slug}")
    public Map<String, Object> updateCategory(HttpServletResponse response, @PathVariable String slug, @RequestBody CreateCategoryForm createCategoryForm){
        Category category = new Category();
        categoryService.updateCategory(slug,createCategoryForm.getTitle(),createCategoryForm.getDescription());
        category = categoryService.getCategories().stream().filter(c -> c.getSlug().equals(slug)).findFirst().orElse(null);

        if(category == null){
            response.setStatus(404);
            return null;
        }
        category.setTitle(createCategoryForm.getTitle());
        category.setDescription(createCategoryForm.getDescription());
        category.setSlug(createCategoryForm.getTitle().replace(" ",""));
        category.setColor(Utils.randomColor());

        Map<String,Object> categoryMap = new HashMap<>();

        categoryMap.put("_id",category.getId());
        categoryMap.put("title",category.getTitle());
        categoryMap.put("description",category.getDescription());
        categoryMap.put("slug",category.getSlug());
        categoryMap.put("color",category.getColor());
        categoryMap.put("__v",0);
        categoryMap.put("moderators",new String[]{});

        return categoryMap;
    }

    //delete
    @DeleteMapping("/categories/{slug}")
    public void deleteCategory(HttpServletResponse response, @PathVariable String slug){
        Category category=  categoryService.getCategoryBySlug(slug);
        List<Topic> topics = topicService.getTopicsByCategory(category);
        for(Topic topic : topics){
            if(topic.getCategory().getSlug().equals(slug)){

                List<Reply> replies = replyService.getRepliesByTopicId(topic.getId());
                for(Reply reply : replies){

                    replyService.deleteReply(reply.getId());

                }
            }
            topicService.deleteTopic(topic.getId(),topic.getUser().getEmail());
        }
        categoryService.removeCategory(slug);

    }
}
