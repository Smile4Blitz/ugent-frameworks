package be.ugent.reeks1.repository;

import org.springframework.stereotype.Service;

import be.ugent.reeks1.components.BlogPost;
import be.ugent.reeks1.exceptions.BlogPostNotFoundException;

import java.util.Collection;
import java.util.HashMap;

@Service
public final class BlogPostDaoMemory implements IBlogPostDAO {
    private final HashMap<Integer, BlogPost> collection = new HashMap<>();

    public Collection<BlogPost> getCollection() {
        return collection.values();
    }

    public void addPost(BlogPost n) {
        this.collection.put(n.getId(), n);
    }

    public void removePost(Integer n) throws BlogPostNotFoundException {
        if (!(this.collection.containsKey(n))) {
            throw new BlogPostNotFoundException();
        } else {
            this.collection.remove(n);
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