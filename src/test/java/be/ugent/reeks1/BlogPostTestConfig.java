package be.ugent.reeks1;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import be.ugent.reeks1.components.BlogPost;
import be.ugent.reeks1.repository.BlogPostDaoMemory;
import be.ugent.reeks1.repository.IBlogPostDAO;

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

