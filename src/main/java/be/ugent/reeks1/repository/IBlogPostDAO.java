package be.ugent.reeks1.repository;

import java.util.Collection;

import be.ugent.reeks1.error.BlogPostNotFoundException;
import be.ugent.reeks1.model.BlogPost;

public interface IBlogPostDAO {

    Collection<BlogPost> getCollection();

    void addPost(BlogPost n);

    void removePost(Integer n) throws BlogPostNotFoundException;

    BlogPost getPost(Integer n) throws BlogPostNotFoundException;

    boolean postExists(Integer n);

}