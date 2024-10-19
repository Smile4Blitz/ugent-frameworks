package be.ugent.reeks1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.ugent.reeks1.components.BlogPost;

public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {
}
