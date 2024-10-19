package be.ugent.reeks1.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ugent.reeks1.components.BlogPost;
import be.ugent.reeks1.exceptions.BlogPostNotFoundException;

@Service
public class BlogPostH2Memory implements IBlogPostDAO {
    private final BlogPostRepository blogPostRepository;

    @Autowired
    public BlogPostH2Memory(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @Override
    public Collection<BlogPost> getCollection() {
        return blogPostRepository.findAll();
    }

    @Override
    public void addPost(BlogPost n) {
        blogPostRepository.saveAndFlush(n);
    }

    @Override
    public void removePost(Integer n) throws BlogPostNotFoundException {
        Optional<BlogPost> blogPost = blogPostRepository.findById(n);

        if (!blogPost.isPresent()) {
            throw new BlogPostNotFoundException();
        }

        blogPostRepository.deleteById(n);;
    }

    @Override
    public BlogPost getPost(Integer n) throws BlogPostNotFoundException {
        Optional<BlogPost> blogPost = blogPostRepository.findById(n);

        if (!blogPost.isPresent()) {
            throw new BlogPostNotFoundException();
        }

        return (BlogPost) blogPost.get();
    }

    @Override
    public boolean postExists(Integer n) {
        return blogPostRepository.existsById(n);
    }
}
