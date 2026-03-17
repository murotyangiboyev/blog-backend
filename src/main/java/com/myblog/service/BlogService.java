package com.myblog.service;

import com.myblog.model.Blog;
import com.myblog.repository.BlogRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAllByOrderByCreatedAtDesc();
    }

    @Cacheable(value = "blogs", key = "#id")
    public Blog getBlogById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found: " + id));
    }

    public Blog createBlog(String title, String content, String author) {
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setAuthor(author);
        blog.setReadMinutes(estimateReadTime(content));
        return blogRepository.save(blog);
    }

    public Blog updateBlog(Long id, String title, String content) {
        Blog blog = getBlogById(id);
        blog.setTitle(title);
        blog.setContent(content);
        blog.setReadMinutes(estimateReadTime(content));
        return blogRepository.save(blog);
    }

    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    private int estimateReadTime(String content) {
        int wordCount = content.trim().split("\\s+").length;
        return Math.max(1, wordCount / 200);
    }
}
