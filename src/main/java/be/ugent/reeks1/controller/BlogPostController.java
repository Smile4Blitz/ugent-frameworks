package be.ugent.reeks1.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import be.ugent.reeks1.components.BlogPost;
import be.ugent.reeks1.exceptions.BlogPostNotFoundException;
import be.ugent.reeks1.repository.IBlogPostDAO;
import be.ugent.reeks1.services.Metrics;

import java.util.Collection;
import java.util.List;
import java.util.HashMap;

@RestController
public class BlogPostController {
    private final IBlogPostDAO memory;
    private final Metrics metrics;

    // @Qualifier("blogPostDaoMemory")
    // @Qualifier("blogPostH2Memory")
    public BlogPostController(@Qualifier("blogPostH2Memory") IBlogPostDAO m, Metrics metrics) {
        this.memory = m;
        this.metrics = metrics;
    }

    @GetMapping("/blogposts")
    public Collection<BlogPost> blogposts(@RequestParam(required = false) List<String> id) {
        HashMap<Integer, BlogPost> collection = new HashMap<>();

        if (id != null) {
            for (String idParam : id) {
                try {
                    BlogPost p = memory.getPost(Integer.parseInt(idParam));
                    collection.put(p.getId(), p);
                } catch (BlogPostNotFoundException | NumberFormatException ignored) {
                    continue;
                }
            }

            metrics.increaseReadCollection();
            return collection.values();
        }

        metrics.increaseReadCollection();
        return memory.getCollection();
    }

    @GetMapping("/blogpost/{id}")
    public BlogPost blogpost_id(@PathVariable("id") Integer id) {
        try {
            BlogPost p = memory.getPost(id);
            metrics.increaseRead();
            return p;
        } catch (BlogPostNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID " + id + " not found.", e);
        }
    }

    @PostMapping("/blogpost/{id}")
    public ResponseEntity<Void> addBlogpost_id(UriComponentsBuilder uriComponentsBuilder,
            @PathVariable("id") Integer id, @RequestBody() BlogPost post) {
        if (id == null || post == null || !id.equals(post.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        try {
            if (!memory.postExists(id)) {
                memory.addPost(post);
            }
        } catch (Exception ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        metrics.increaseCreate();
        UriComponents uriComponents = uriComponentsBuilder.path("/blogpost/{id}").buildAndExpand(id);
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @DeleteMapping("/blogpost/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Integer id) {
        try {
            memory.removePost(id);
        } catch (BlogPostNotFoundException ignored) {
            return ResponseEntity.notFound().build();
        }

        metrics.increaseDelete();
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/blogpost/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable("id") Integer id, @RequestBody BlogPost post) {
        if (id != post.getId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        try {
            memory.removePost(id);
        } catch (BlogPostNotFoundException ignored) {
            throw new ResponseStatusException(HttpStatus.valueOf(409));
        }

        metrics.increaseUpdate();
        memory.addPost(post);
        return ResponseEntity.status(204).build();
    }
}
