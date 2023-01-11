package com.cmit.kapok.system.controller.hello;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HelloWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/hello-assets/**").addResourceLocations("classpath:/hello-assets/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
    
}
