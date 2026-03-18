package com.myblog.controller;

import com.myblog.model.Blog;
import com.myblog.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/blog")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    // PUBLIC: list all posts
    @GetMapping
    public String list(Model model) {
        model.addAttribute("blogs", blogService.getAllBlogs());
        return "blog-list";
    }

    // PUBLIC: read one post
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Blog blog = blogService.getBlogById(id);
        model.addAttribute("blog", blog);
        // processedContent is used in the template for display (HTML-rendered)
        // blog.content is kept raw for the edit form
        model.addAttribute("processedContent", blogService.processForDisplay(blog.getContent()));
        return "blog-detail";
    }

    // ADMIN: show write form
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "blog-new";
    }

    // ADMIN: save new post
    @PostMapping
    public String create(@RequestParam String title,
                         @RequestParam String content,
                         Principal principal) {
        blogService.createBlog(title, content, principal.getName());
        return "redirect:/blog";
    }

    // ADMIN: show edit form
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getBlogById(id));
        return "blog-edit";
    }

    // ADMIN: save updated post
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @RequestParam String title,
                         @RequestParam String content) {
        blogService.updateBlog(id, title, content);
        return "redirect:/blog/" + id;
    }

    // ADMIN: delete post
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return "redirect:/blog";
    }
}
