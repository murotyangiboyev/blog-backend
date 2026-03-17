-- ============================================================
-- Blog Database Setup Script
-- Run this once before starting the application
-- ============================================================

-- 1. Create database (run as postgres superuser)
CREATE DATABASE blogdb;

-- 2. Connect to blogdb, then run the rest:
\c blogdb

-- 3. Create posts table
CREATE TABLE IF NOT EXISTS posts (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    slug        VARCHAR(255) NOT NULL UNIQUE,
    summary     VARCHAR(500),
    content     TEXT NOT NULL,
    tags        VARCHAR(255),
    published   BOOLEAN NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP
);

-- 4. Index for faster slug lookups (used in every post page view)
CREATE INDEX IF NOT EXISTS idx_posts_slug ON posts(slug);

-- 5. Index for filtering published posts
CREATE INDEX IF NOT EXISTS idx_posts_published ON posts(published);

-- ============================================================
-- Sample post to test with (optional)
-- ============================================================
INSERT INTO posts (title, slug, summary, content, tags, published, created_at)
VALUES (
    'Hello World — My First Post',
    'hello-world-my-first-post',
    'Welcome to my blog. This is my very first post!',
    '# Hello World

Welcome to my personal blog! I am excited to start writing here.

## What I Will Write About

- Programming and software development
- Things I learn day to day
- Projects I am working on

## Code Example

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

Thanks for reading!',
    'intro, general',
    TRUE,
    NOW()
);
