package be.ugent.reeks1.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import be.ugent.reeks1.error.BlogPostNotFoundException;
import be.ugent.reeks1.model.BlogPost;
import be.ugent.reeks1.repository.IBlogPostDAO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(BlogPostTestConfig.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class BlogPostControllerTest {
    @Autowired
    private WebTestClient wtc;

    @Autowired
    private IBlogPostDAO memory;

    @Value("${admin.username}")
    private String username;

    @Value("${admin.password}")
    private String password;

    @Test
    void readBlogPostsJSON() {
        wtc
                .get().uri("/blogposts")
                .header("Accept", "application/json")
                .headers(headers -> headers.setBasicAuth(username, password))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BlogPost.class)
                .hasSize(4);
    }

    @Test
    void readBlogPostsXML() {
        XmlMapper xmlMapper = new XmlMapper();
        Collection<BlogPost> blogPosts = memory.getCollection();
        String collection;

        try {
            collection = xmlMapper
                    .writer()
                    .withRootName("Collection") // default rootname = "<Values>"
                    .writeValueAsString(blogPosts);

            wtc
                    .get().uri("/blogposts")
                    .header("Accept", "application/xml")
                    .headers(headers -> headers.setBasicAuth(username, password))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(String.class)
                    .isEqualTo(collection);
        } catch (JsonProcessingException e) {
            fail(e);
        }
    }

    @Test
    void readBlogPost() {
        for (int id = 1; id < 4; id++) {
            try {
                BlogPost testPost = memory.getPost(id);
                wtc
                        .get()
                        .uri("/blogpost/" + id)
                        .headers(headers -> headers.setBasicAuth(username, password))
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(BlogPost.class);

                try {
                    assertEquals(testPost.getId(), memory.getPost(id).getId());
                    assertEquals(testPost.getTitle(), memory.getPost(id).getTitle());
                    assertEquals(testPost.getContent(), memory.getPost(id).getContent());
                } catch (BlogPostNotFoundException e) {
                    fail(e);
                }
            } catch (BlogPostNotFoundException e) {
                fail(e);
            }
        }
    }

    //@Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void postBlogPosts() throws Exception {
        Integer id = 25;
        String title = "test";
        String testContent = "testContent";

        BlogPost testPost = new BlogPost(id, title, testContent);

        wtc
                .post().uri("/blogpost/" + id)
                .headers(headers -> headers.setBasicAuth(username, password))
                .bodyValue(testPost)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueMatches("Location", "^http://[a-zA-Z0-9]+:[0-9]+/blogpost/" + id + "$");

        try {
            assertEquals(testPost.getId(), memory.getPost(id).getId());
            assertEquals(testPost.getTitle(), memory.getPost(id).getTitle());
            assertEquals(testPost.getContent(), memory.getPost(id).getContent());
        } catch (BlogPostNotFoundException e) {
            fail(e);
        }
    }

    //@Test
    @DirtiesContext
    void putBlogPosts() {
        Integer id = 1;
        String title = "putTitle";
        String testContent = "testContent";

        BlogPost testPost = new BlogPost(id, title, testContent);

        wtc
                .put().uri("/blogpost/" + id)
                .headers(headers -> headers.setBasicAuth(username, password))
                .bodyValue(testPost)
                .exchange()
                .expectStatus().isNoContent();

        try {
            assertEquals(testPost.getId(), memory.getPost(id).getId());
            assertEquals(testPost.getTitle(), memory.getPost(id).getTitle());
            assertEquals(testPost.getContent(), memory.getPost(id).getContent());
        } catch (BlogPostNotFoundException e) {
            fail(e);
        }

    }

    //@Test
    @DirtiesContext
    void deleteBlogPost() {
        Integer id = 1;

        wtc
                .delete().uri("/blogpost/" + id)
                .headers(headers -> headers.setBasicAuth(username, password))
                .exchange()
                .expectStatus().isEqualTo(201);

        assertThrows(BlogPostNotFoundException.class, () -> memory.getPost(id));
    }
}
