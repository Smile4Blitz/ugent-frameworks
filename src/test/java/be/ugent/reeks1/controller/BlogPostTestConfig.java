package be.ugent.reeks1.controller;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import be.ugent.reeks1.model.BlogPost;
import be.ugent.reeks1.repository.IBlogPostDAO;
import be.ugent.reeks1.services.BlogPostDaoMemory;

@TestConfiguration
public class BlogPostTestConfig {
    @Bean
    @Primary
    public IBlogPostDAO bogusBlogPostDaoMemory() {
        IBlogPostDAO memory = new BlogPostDaoMemory();

        memory.addPost(new BlogPost(1, "title", "content"));
        memory.addPost(new BlogPost(2, "abc", "123"));
        memory.addPost(new BlogPost(3, "def", "456"));
        memory.addPost(new BlogPost(4, "ghi", "789"));
        
        return memory;
    }
}

