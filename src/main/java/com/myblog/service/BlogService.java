package com.myblog.service;

import com.myblog.model.Blog;
import com.myblog.repository.BlogRepository;
import org.springframework.cache.annotation.CacheEvict;
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
        System.out.println("DB HIT id:" + id);
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

    @CacheEvict(value = "blogs", key = "#id")
    public Blog updateBlog(Long id, String title, String content) {
        Blog blog = getBlogById(id);
        blog.setTitle(title);
        blog.setContent(content);
        blog.setReadMinutes(estimateReadTime(content));
        return blogRepository.save(blog);
    }

    @CacheEvict(value = "blogs", key = "#id")
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    public String processForDisplay(String content) {
        if (content == null) return "";

        // 1. Convert [text](url) to <a href="url">text</a>
        content = content.replaceAll(
                "\\[([^\\]]+)\\]\\(([^)]+)\\)",
                "<a href=\"$2\" target=\"_blank\" rel=\"noopener\">$1</a>"
        );

        // 2. Split by blank lines → wrap each block in <p>
        String[] paragraphs = content.split("\\n\\s*\\n");
        StringBuilder html = new StringBuilder();
        for (String para : paragraphs) {
            String trimmed = para.trim();
            if (!trimmed.isEmpty()) {
                // 3. Within a paragraph, single newlines → <br>
                trimmed = trimmed.replace("\n", "<br>");
                html.append("<p>").append(trimmed).append("</p>\n");
            }
        }

        return html.toString();
    }

    private int estimateReadTime(String content) {
        int wordCount = content.trim().split("\\s+").length;
        return Math.max(1, wordCount / 200);
    }
}
