# 🐳 Docker & Docker Compose Setup for AcadLink Backend

This guide explains how to containerize the **Spring Boot backend** and **PostgreSQL database** using Docker and Docker Compose.

---

## 📁 Project Structure

```
acadlink-backend/
├── backend/
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── ...
├── docker-compose.yml
└── .env
```

---

## ⚙️ Step 1: Create `.env` File

Add your environment variables for Docker Compose.

```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres123
POSTGRES_DB=acadlink
POSTGRES_PORT=5432
SPRING_PORT=8081
```

---

## 🧱 Step 2: Dockerfile (in `/backend`)

```dockerfile
# Use official OpenJDK image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for dependency caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the project
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8081

# Run the Spring Boot app
ENTRYPOINT ["java","-jar","target/backend-0.0.1-SNAPSHOT.jar"]
```

---

## 🐘 Step 3: docker-compose.yml

Create a `docker-compose.yml` file at the root:

```yaml
version: '3.9'

services:
  postgres:
    image: postgres:17
    container_name: acadlink_postgres
    restart: always
    environment:
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_DB: ${POSTGRES_DB}
    ports:
      - "${POSTGRES_PORT}:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data
    networks:
      - acadlink-network

  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    container_name: acadlink_backend
    restart: always
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/${POSTGRES_DB}
      SPRING_DATASOURCE_USERNAME: ${POSTGRES_USER}
      SPRING_DATASOURCE_PASSWORD: ${POSTGRES_PASSWORD}
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_PROFILES_ACTIVE: dev
    ports:
      - "${SPRING_PORT}:8081"
    networks:
      - acadlink-network

volumes:
  pgdata:

networks:
  acadlink-network:
    driver: bridge
```

---

## ⚡ Step 4: Update `application.yml`

Make sure your Spring Boot app reads environment variables correctly:

```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/acadlink}
    username: ${SPRING_DATASOURCE_USERNAME:postgres}
    password: ${SPRING_DATASOURCE_PASSWORD:postgres123}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

  flyway:
    enabled: true
    locations: classpath:db/migration
```

---

## 🚀 Step 5: Run the Containers

Build and start your containers:

```bash
docker compose up --build
```

This will:
- Start **PostgreSQL 17**
- Build and start **Spring Boot backend**
- Connect backend automatically to database

Visit your API at 👉 **http://localhost:8081**

---

## 🧹 Step 6: Managing Containers

```bash
# Stop containers
docker compose down

# Stop and remove database volume (clean database)
docker compose down -v

# Rebuild everything
docker compose up --build
```

---

## 🧩 Step 7: Tips & Best Practices

✅ **Volumes**
- Keeps database data persistent between container restarts.

✅ **Networks**
- The backend communicates with the database using the Docker service name (`postgres`) instead of `localhost`.

✅ **Environment Variables**
- Keep `.env` file in `.gitignore` (never commit secrets).

✅ **Health Check**
- You can add a `healthcheck:` section in `docker-compose.yml` for better orchestration.

✅ **Production Setup**
- Use separate Docker Compose files (e.g., `docker-compose.prod.yml`) for production overrides.

---

## ✅ Verification

To confirm everything is working:
1. Run `docker ps` → You should see both containers running.
2. Run `docker logs acadlink_backend` → Check Spring Boot startup logs.
3. Visit `http://localhost:8081/health` → Verify backend health.
4. Connect to DB via `psql`:

   ```bash
   docker exec -it acadlink_postgres psql -U postgres -d acadlink
   ```

---

**Author:** Paras Jain  
**Project:** AcadLink — University Collaboration Platform  
**Tech Stack:** Spring Boot 3.5.6 • PostgreSQL 17 • Docker Compose
