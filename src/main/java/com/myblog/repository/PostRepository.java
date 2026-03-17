package com.myblog.repository;

import com.myblog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Find published posts ordered by newest first (for public blog list)
    List<Post> findByPublishedTrueOrderByCreatedAtDesc();

    // Find a post by its slug (used in URLs like /blog/my-first-post)
    Optional<Post> findBySlug(String slug);

    // Find published post by slug (for public view)
    Optional<Post> findBySlugAndPublishedTrue(String slug);

    // Search posts by title (for admin)
    List<Post> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);

    // All posts ordered by date (for admin panel)
    List<Post> findAllByOrderByCreatedAtDesc();

    // Check if slug already exists (to avoid duplicates)
    boolean existsBySlug(String slug);
}
