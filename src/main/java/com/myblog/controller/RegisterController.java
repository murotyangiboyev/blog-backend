package com.myblog.controller;

import com.myblog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model) {

        // 1. Check passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("username", username);
            return "register";
        }

        // 2. Check username length
        if (username.length() < 3) {
            model.addAttribute("error", "Username must be at least 3 characters.");
            return "register";
        }

        // 3. Check password length
        if (password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters.");
            model.addAttribute("username", username);
            return "register";
        }

        // 4. Try to register
        try {
            userService.registerUser(username, password);
            model.addAttribute("success", "Account created! You can now sign in.");
            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            return "register";
        }
    }
}
