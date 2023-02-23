package com.esliceu.webservice;

import com.esliceu.webservice.interceptors.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class  WebServiceApplication implements WebMvcConfigurer {
    @Autowired
    TokenInterceptor tokenInterceptor;
    public static void main(String[] args) {
        SpringApplication.run(WebServiceApplication.class, args);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/profile/password");
    }
}
