package be.ugent.reeks1.repository;

import java.util.Collection;

import be.ugent.reeks1.components.BlogPost;
import be.ugent.reeks1.exceptions.BlogPostNotFoundException;

public interface IBlogPostDAO {

    Collection<BlogPost> getCollection();

    void addPost(BlogPost n);

    void removePost(Integer n) throws BlogPostNotFoundException;

    BlogPost getPost(Integer n) throws BlogPostNotFoundException;

    boolean postExists(Integer n);

}