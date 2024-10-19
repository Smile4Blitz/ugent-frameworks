package be.ugent.reeks1.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import be.ugent.reeks1.error.BlogPostNotFoundException;
import be.ugent.reeks1.model.BlogPost;
import be.ugent.reeks1.repository.IBlogPostDAO;

import java.util.Collection;
import java.util.HashMap;

@Service
@Profile("test")
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