package be.ugent.reeks1.services;

import org.springframework.stereotype.Service;

import be.ugent.reeks1.components.BlogPost;
import be.ugent.reeks1.exceptions.BlogPostNotFoundException;

import java.util.Collection;
import java.util.HashMap;

@Service
public class BlogPostDaoMemory {
    private final HashMap<Integer, BlogPost> collection = new HashMap<>();

    public Collection<BlogPost> getCollection() {
        return collection.values();
    }

    public void addPost(BlogPost n) {
        this.collection.put(n.getId(),n);
    }

    public void removePost(Integer n) throws BlogPostNotFoundException {
        if(this.collection.remove(n) == null) {
            throw new BlogPostNotFoundException();
        }
    }

    public BlogPost getPost(Integer n) throws BlogPostNotFoundException {
        BlogPost p = collection.get(n);
        if (p == null)
            throw new BlogPostNotFoundException();
        else
            return p;
    }

    public boolean postExists(Integer n) {
        return collection.containsKey(n);
    }
}