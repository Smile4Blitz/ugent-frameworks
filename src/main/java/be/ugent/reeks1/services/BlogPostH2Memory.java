package be.ugent.reeks1.services;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import be.ugent.reeks1.error.BlogPostNotFoundException;
import be.ugent.reeks1.model.BlogPost;
import be.ugent.reeks1.repository.BlogPostRepository;
import be.ugent.reeks1.repository.IBlogPostDAO;

@Service
@Profile("prod")
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
