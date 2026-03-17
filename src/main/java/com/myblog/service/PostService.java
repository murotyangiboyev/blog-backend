package com.myblog.service;

import com.myblog.model.Post;
import com.myblog.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // ─── Public Blog ───────────────────────────────────────────────

    /** Returns all published posts, newest first */
    public List<Post> getPublishedPosts() {
        return postRepository.findByPublishedTrueOrderByCreatedAtDesc();
    }

    /** Returns a published post by its slug for public viewing */
    public Optional<Post> getPublishedPostBySlug(String slug) {
        return postRepository.findBySlugAndPublishedTrue(slug);
    }

    // ─── Admin ─────────────────────────────────────────────────────

    /** Returns ALL posts (published + drafts) for admin panel */
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    /** Returns any post by id (for admin editing) */
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    /** Creates a new post. Auto-generates slug from title. */
    public Post createPost(Post post) {
        String slug = generateUniqueSlug(post.getTitle());
        post.setSlug(slug);
        return postRepository.save(post);
    }

    /** Updates an existing post */
    public Post updatePost(Long id, Post updatedPost) {
        Post existing = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found: " + id));

        existing.setTitle(updatedPost.getTitle());
        existing.setSummary(updatedPost.getSummary());
        existing.setContent(updatedPost.getContent());
        existing.setTags(updatedPost.getTags());
        existing.setPublished(updatedPost.isPublished());

        // Only regenerate slug if title changed
        if (!existing.getTitle().equals(updatedPost.getTitle())) {
            existing.setSlug(generateUniqueSlug(updatedPost.getTitle()));
        }

        return postRepository.save(existing);
    }

    /** Deletes a post by id */
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    /** Toggle publish/draft status */
    public Post togglePublished(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found: " + id));
        post.setPublished(!post.isPublished());
        return postRepository.save(post);
    }

    // ─── Slug Generation ───────────────────────────────────────────

    /**
     * Converts title to URL slug.
     * "Hello World! This is Java" -> "hello-world-this-is-java"
     */
    public String generateUniqueSlug(String title) {
        String base = toSlug(title);
        String slug = base;
        int counter = 1;

        while (postRepository.existsBySlug(slug)) {
            slug = base + "-" + counter;
            counter++;
        }
        return slug;
    }

    private String toSlug(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized
                .replaceAll("[^\\p{ASCII}]", "")   // remove non-ascii
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")   // remove special chars
                .replaceAll("\\s+", "-")             // spaces to hyphens
                .replaceAll("-+", "-")               // collapse multiple hyphens
                .replaceAll("^-|-$", "");            // trim leading/trailing hyphens
    }
}
