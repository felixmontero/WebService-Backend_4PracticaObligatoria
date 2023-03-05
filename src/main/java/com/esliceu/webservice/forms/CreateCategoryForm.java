package com.esliceu.webservice.forms;

public class CreateCategoryForm {

    private String title;
    private String description;
    private String slug;
    private String color;

    public CreateCategoryForm() {
    }

    public CreateCategoryForm(String title, String description, String slug, String color) {
        this.title = title;
        this.description = description;
        this.slug = slug;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
