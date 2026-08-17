# Personal Blog — Spring Boot

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

---

## Deployment

Every setting in `application.properties` reads from an environment variable with a
localhost fallback, so the same JAR runs locally and in the cloud with no code changes.

### Run the whole stack with Docker

```bash
cp .env.example .env     # then edit .env and set real passwords
docker compose up -d --build
docker compose logs -f app
```

This starts Postgres, Redis (master + replica), RabbitMQ, and the app on
`http://localhost:8080`. Data lives in named volumes and survives restarts.

### Deploy to a VPS (Hetzner, DigitalOcean, EC2, …)

1. Install Docker + the Compose plugin on the server.
2. Copy the repo over (`git clone`, or `rsync` the working tree).
3. Create `.env` on the server with production passwords — never commit it.
4. `docker compose up -d --build`
5. Put a reverse proxy in front for HTTPS. With Caddy, a two-line `Caddyfile`
   is enough:

   ```
   blog.example.com {
       reverse_proxy localhost:8080
   }
   ```

   The `prod` profile sets `server.forward-headers-strategy=native`, so Spring
   generates `https://` redirects correctly behind the proxy.

### Deploy to a PaaS (Railway, Render, Fly.io)

The Dockerfile works as-is. Provision managed Postgres and Redis add-ons, plus
CloudAMQP for RabbitMQ, then set these environment variables in the dashboard:

| Variable | Notes |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://host:5432/dbname` |
| `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD` | from the add-on |
| `REDIS_MASTER_HOST` / `REDIS_MASTER_PORT` | from the add-on |
| `REDIS_SLAVE_HOST` / `REDIS_SLAVE_PORT` | point at the **same** instance if there's no replica |
| `RABBITMQ_HOST` / `RABBITMQ_PORT` / `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD` | from CloudAMQP |
| `BLOG_ADMIN_USERNAME` / `BLOG_ADMIN_PASSWORD` | admin account, created on first boot |

Health check endpoint for the platform's probes: `GET /actuator/health`.

### Schema

`spring.jpa.hibernate.ddl-auto` defaults to `update`, so Hibernate creates the
`blogs` and `users` tables on first startup — no manual SQL needed.
(`database/setup.sql` is stale: it creates a `posts` table that no entity maps to.)
Once the schema is stable, set `JPA_DDL_AUTO=validate` and manage changes with
Flyway or Liquibase instead of letting Hibernate alter production tables.
