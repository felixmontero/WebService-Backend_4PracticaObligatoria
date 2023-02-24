package com.esliceu.webservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.lang.reflect.Array;

@Entity
public class Categories {
    @Id
    private int id;

    private String title;

    private String description;

    private String slug;

    private String color;

    private int __v;

}
