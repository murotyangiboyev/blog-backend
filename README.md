# Personal Blog — Spring Boot + PostgreSQL

A clean, minimal personal blog.

## Tech Stack
- **Backend:** Java 17 + Spring Boot 3.2
- **Frontend:** Thymeleaf (HTML rendered by Spring)
- **Database:** PostgreSQL
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

### Step 2 — Create the Database

```bash
# Open psql as postgres user
psql -U postgres

# Run the setup script
\i database/setup.sql
```

Or manually:
```sql
CREATE DATABASE blogdb;
```

### Step 3 — Configure the App

Edit `src/main/resources/application.properties`:

```properties
# Change these to match your PostgreSQL setup
spring.datasource.url=jdbc:postgresql://localhost:5432/blogdb
spring.datasource.username=postgres
spring.datasource.password=your_password_here

# Your blog profile
blog.owner.name=Your Name
blog.owner.title=Software Developer

# Admin login — CHANGE THESE
blog.admin.username=admin
blog.admin.password=changeme123
```

### Step 4 — Add Your Profile Photo

Put your photo at:
```
src/main/resources/static/images/profile.jpg
```

### Step 5 — Run the App

```bash
mvn spring-boot:run
```

---

## Pages

| URL | Description |
|-----|-------------|
| `http://localhost:8080/` | Homepage |
| `http://localhost:8080/blog` | All posts list |
| `http://localhost:8080/blog/{slug}` | Single post |
| `http://localhost:8080/admin` | Admin dashboard |
| `http://localhost:8080/login` | Admin login |

---

## Writing Posts

1. Go to `http://localhost:8080/admin`
2. Login with your admin credentials
3. Click **New Post**
4. Write content in **Markdown**
5. Choose **Publish** or save as **Draft**

### Markdown Cheatsheet

```markdown
# Heading 1
## Heading 2

**bold text**
*italic text*

- bullet list
- item 2

1. numbered list
2. item 2

[link text](https://example.com)

![image alt](image-url.jpg)

`inline code`

\`\`\`java
// code block
public class Example { }
\`\`\`

| Column 1 | Column 2 |
|----------|----------|
| value    | value    |
```

---

## Next Steps (Phase 2 — Frontend)

After the backend is verified working, we will add:
- [ ] Thymeleaf HTML templates
- [ ] CSS styling (minimal, clean like asadullo.com)
- [ ] Syntax highlighting for code blocks (Prism.js)
- [ ] RSS feed
- [ ] Tags/filtering
