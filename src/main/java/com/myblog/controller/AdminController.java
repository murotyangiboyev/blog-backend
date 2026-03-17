package com.myblog.controller;

import com.myblog.model.Post;
import com.myblog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PostService postService;

    public AdminController(PostService postService) {
        this.postService = postService;
    }

    /** GET /admin → Dashboard: list all posts */
    @GetMapping
    public String dashboard(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        long publishedCount = posts.stream().filter(Post::isPublished).count();
        long draftCount = posts.size() - publishedCount;
        model.addAttribute("publishedCount", publishedCount);
        model.addAttribute("draftCount", draftCount);
        return "admin/dashboard"; // templates/admin/dashboard.html
    }

    /** GET /admin/posts/new → Show create post form */
    @GetMapping("/posts/new")
    public String newPostForm(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("isEdit", false);
        return "admin/post-form"; // templates/admin/post-form.html
    }

    /** POST /admin/posts → Save new post */
    @PostMapping("/posts")
    public String createPost(@ModelAttribute Post post,
                             RedirectAttributes redirectAttributes) {
        try {
            postService.createPost(post);
            redirectAttributes.addFlashAttribute("success", "Post created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating post: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    /** GET /admin/posts/{id}/edit → Show edit form */
    @GetMapping("/posts/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) {
        Optional<Post> postOpt = postService.getPostById(id);
        if (postOpt.isEmpty()) {
            return "redirect:/admin";
        }
        model.addAttribute("post", postOpt.get());
        model.addAttribute("isEdit", true);
        return "admin/post-form";
    }

    /** POST /admin/posts/{id} → Update existing post */
    @PostMapping("/posts/{id}")
    public String updatePost(@PathVariable Long id,
                             @ModelAttribute Post post,
                             RedirectAttributes redirectAttributes) {
        try {
            postService.updatePost(id, post);
            redirectAttributes.addFlashAttribute("success", "Post updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating post: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    /** POST /admin/posts/{id}/delete → Delete a post */
    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        try {
            postService.deletePost(id);
            redirectAttributes.addFlashAttribute("success", "Post deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting post.");
        }
        return "redirect:/admin";
    }

    /** POST /admin/posts/{id}/toggle → Publish or unpublish */
    @PostMapping("/posts/{id}/toggle")
    public String togglePublish(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        try {
            Post post = postService.togglePublished(id);
            String status = post.isPublished() ? "published" : "set to draft";
            redirectAttributes.addFlashAttribute("success", "Post " + status + ".");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error toggling post status.");
        }
        return "redirect:/admin";
    }
}
