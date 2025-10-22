# 🧭 Database Migration Guide — AcadLink Backend

This document defines the **rules, best practices, and workflows** for creating, managing, and executing database migrations in the AcadLink backend.

---

## 📘 Overview

AcadLink uses **[Flyway](https://flywaydb.org/)** as the database migration tool for PostgreSQL.  
Flyway ensures consistent schema evolution across environments (local, staging, production) through versioned SQL files.

Each migration file represents a **single, incremental schema change** in the database.

---

## 🗂️ Folder Structure

All migrations are stored in:
```
src/main/resources/db/migration/
```

Example:
```
V1__init_schema.sql
V2__create_university_table.sql
V3__create_professor_table.sql
V4__add_student_table.sql
```

---

## 🧱 File Naming Conventions

| Rule | Description | Example |
|------|--------------|----------|
| Must start with `V` | Denotes versioned migration | `V1__init_schema.sql` |
| Version is numeric only | Increment sequentially | `V2__create_users_table.sql` |
| Separate version and description with `__` (double underscore) | Required by Flyway | `V3__add_foreign_keys.sql` |
| Use lowercase and underscores | Consistent across all migrations | `V4__rename_column_in_student.sql` |
| Keep description short and action-based | Describe the change, not the feature | `V5__drop_unused_table.sql` |

✅ Example naming pattern:
```
V{version_number}__{action_description}.sql
```

---

## 🧩 Writing a Migration

### ✅ Do’s
- Use **SQL syntax compatible with PostgreSQL**.
- Always use **safe / idempotent statements** when possible.
- Include **clear comments** in SQL for readability.
- Keep each migration **small, atomic, and descriptive**.
- Test locally on a **fresh database** before committing.

### 🚫 Don’ts
- ❌ Do not modify an already executed migration file.
- ❌ Do not reuse a version number.
- ❌ Do not include environment-specific data.
- ❌ Do not perform massive data migrations inline with schema changes.

---

## ⚙️ Running Migrations

Flyway automatically runs migrations when the Spring Boot application starts:
```bash
./mvnw spring-boot:run
```

Or manually:
```bash
mvn flyway:migrate
```

For clean (⚠️ only for dev/local use):
```bash
mvn flyway:clean
```

> ⚠️ **Never use `flyway.clean` in production.** It will drop all tables in the connected database.

---

## 🔄 Migration Workflow

1. Pull latest code and migrations.
2. Create a new migration file.
3. Write and test migration locally.
4. Commit and push with related code.
5. Merge to main after testing on staging.

---

## 🔒 Validation and Safety Rules

| Rule | Description |
|------|--------------|
| 🔹 `validate-on-migrate: true` | Ensures applied migrations match current scripts |
| 🔹 `outOfOrder: false` | Prevents running older migrations accidentally |
| 🔹 Backup before deploy | Always back up the database before running production migrations |
| 🔹 Test on staging | Apply migrations in staging before production rollout |
| 🔹 Track schema history | Never manually modify `flyway_schema_history` |

---

## 🧠 Examples

### Create a new table
```sql
CREATE TABLE IF NOT EXISTS professors (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    university_id UUID REFERENCES universities(id),
    created_at TIMESTAMP DEFAULT NOW()
);
```

### Alter an existing table
```sql
ALTER TABLE professors
ADD COLUMN IF NOT EXISTS phone_number VARCHAR(20);
```

### Drop unused column
```sql
ALTER TABLE students
DROP COLUMN IF EXISTS temp_data;
```

---

## 🧰 Useful Commands

| Command | Description |
|----------|-------------|
| `mvn flyway:info` | Show current migration status |
| `mvn flyway:migrate` | Apply pending migrations |
| `mvn flyway:validate` | Validate applied migrations |
| `mvn flyway:repair` | Fix checksums after failed migration (use cautiously) |
| `mvn flyway:clean` | Drop all objects in DB (for local testing only) |

---

## ✅ Summary

| Category | Best Practice |
|-----------|----------------|
| **Naming** | `Vx__description.sql` with clear, sequential versions |
| **Safety** | Never modify past migrations |
| **Testing** | Always test migrations locally/staging |
| **Commit Rules** | Migrate + entity code together |
| **Collaboration** | Resolve version conflicts before merging |
| **Backup** | Always backup before production migrations |

---

**Maintained by:** Backend Team – AcadLink  
**Last updated:** October 2025
