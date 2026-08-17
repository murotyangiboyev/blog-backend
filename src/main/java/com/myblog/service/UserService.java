package com.myblog.service;

import com.myblog.model.User;
import com.myblog.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String password) {
        // Check if username already taken
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username '" + username + "' is already taken.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER"); // registered users get ROLE_USER, not ROLE_ADMIN

        return userRepository.save(user);
    }
}
