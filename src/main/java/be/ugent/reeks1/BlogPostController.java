package be.ugent.reeks1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.websocket.server.PathParam;

import java.util.Collection;
import java.util.List;
import java.util.HashMap;

@RestController
public class BlogPostController {
    private final BlogPostDaoMemory memory;

    public BlogPostController(BlogPostDaoMemory m) {
        this.memory = m;
        BogusBlogPostDaoMemory();
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
        }

        return collection.values();
    }

    @GetMapping("/blogpost/{id}")
    public BlogPost blogpost_id(@PathVariable("id") Integer id) {
        try {
            return memory.getPost(id);
        } catch (BlogPostNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID " + id + " not found.", e);
        }
    }

    @PostMapping("/blogpost/{id}")
    public ResponseEntity<Void> addBlogpost_id(UriComponentsBuilder uriComponentsBuilder, @PathVariable("id") Integer id ,@RequestBody() BlogPost post) {
        if(id == null || post == null || !id.equals(post.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        
        try {
            if(!memory.postExists(id)) {
                memory.addPost(post);
            }
        } catch(Exception ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        UriComponents uriComponents = uriComponentsBuilder.path("/blogpost/{id}").buildAndExpand(id);       
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @DeleteMapping("/blogpost/{id}")
    public ResponseEntity<Void> deletePost(@PathParam("id") Integer id) {
        try {
            memory.removePost(id);
        } catch (BlogPostNotFoundException ignored) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(201).build();
    }

    @PutMapping("/blogpost/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable("id") Integer id, @RequestBody BlogPost post) {
        if(id != post.getId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        try {
            memory.removePost(id);
        } catch (BlogPostNotFoundException ignored) {
            throw new ResponseStatusException(HttpStatus.valueOf(409));
        }

        memory.addPost(post);
        return ResponseEntity.status(204).build();
    }

    private void BogusBlogPostDaoMemory() {
        memory.addPost(new BlogPost(1, "title", "content"));
        memory.addPost(new BlogPost(2, "abc", "123"));
        memory.addPost(new BlogPost(3, "def", "456"));
        memory.addPost(new BlogPost(4, "ghi", "789"));
    }
}
