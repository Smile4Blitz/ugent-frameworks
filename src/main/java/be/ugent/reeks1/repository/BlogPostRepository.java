package be.ugent.reeks1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.ugent.reeks1.model.BlogPost;

public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {
}
