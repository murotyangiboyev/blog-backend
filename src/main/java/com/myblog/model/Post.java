package com.myblog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // URL-friendly version of title: "My First Post" -> "my-first-post"
    @Column(nullable = false, unique = true)
    private String slug;

    // Short description shown in post list
    @Column(length = 500)
    private String summary;

    // Full post content written in Markdown
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // Tags like "java, spring, tutorial"
    @Column
    private String tags;

    @Column(nullable = false)
    private boolean published = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper: returns formatted date like "March 7, 2026"
    public String getFormattedDate() {
        if (createdAt == null) return "";
        return createdAt.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
    }

    // Helper: returns short date like "07 Mar 2026"
    public String getShortDate() {
        if (createdAt == null) return "";
        return createdAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }
}
