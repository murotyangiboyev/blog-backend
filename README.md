# Personal Blog — Spring Boot + PostgreSQL

A clean, minimal personal blog.

## Tech Stack
- **Backend:** Java 17 + Spring Boot 3.2
- **Frontend:** Thymeleaf (HTML rendered by Spring)
- **Database:** PostgreSQL
- **Cache:** Redis
- **Message Queue:** RabbitMQ
- **Markdown:** CommonMark (posts written in Markdown)
- **Auth:** Spring Security (single admin user)

---

## Project Structure

```
blog-backend/
├── pom.xml
├── database/
│   └── setup.sql               ← Run this first!
└── src/main/
    ├── java/com/myblog/
    │   ├── BlogApplication.java
    │   ├── config/
    │   │   └── SecurityConfig.java
    │   ├── controller/
    │   │   ├── BlogController.java   ← Public blog pages
    │   │   ├── AdminController.java  ← Admin CRUD
    │   │   └── AuthController.java   ← Login page
    │   ├── model/
    │   │   └── Post.java
    │   ├── repository/
    │   │   └── PostRepository.java
    │   └── service/
    │       ├── PostService.java
    │       └── MarkdownService.java
    └── resources/
        ├── application.properties  ← Configure DB + your info here
        ├── templates/              ← HTML pages (coming next)
        └── static/                 ← CSS, JS, images
```

---

## Setup Instructions

### Step 1 — Install Requirements
- Java 17+: https://adoptium.net/
- Maven 3.8+: https://maven.apache.org/
- PostgreSQL 14+: https://www.postgresql.org/
