# AcadLink Backend Progress Documentation

## ✅ 1. Core Entities Created (University, Professor, Student)
We created the three foundational entities used across the authentication and relationship model of the app:
- **University**
- **Professor**
- **Student**

### Relationships implemented:
- University ↔ Students → **Many-to-Many**
- University → Professors → **One-to-Many**
- Student ↔ Professor → **Many-to-Many**

These were implemented inside the `V2_create_core_entities.sql` Flyway migration.

---

## ✅ 2. Migration File Added: `V2_create_core_entities.sql`
This migration:
- Creates tables for University, Professor, and Student.
- Adds join tables:
  - `university_students`
  - `student_professors`
- Sets up UUID primary keys.
- Adds unique email constraints.

---

## ✅ 3. Standard API Response Structure Implemented
We implemented:

### **Success Response**
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... }
}
```

### **Error Response**
```json
{
  "success": false,
  "message": "Validation failed",
  "errorCode": "VALIDATION_ERROR"
}
```

We created:
- **ApiResponse** class (generic)
- **ApiErrorResponse**
- **GlobalExceptionHandler**

---

## ✅ 4. Security Layer Implemented (JWT + Spring Security)
We built a complete JWT authentication system including:

### Files implemented:
- `JwtAuthenticationFilter`
- `JwtUtils`
- `UserPrincipal`
- `Role`
- `CustomUserDetailsService`
- `SecurityConfig`
- `JwtAuthenticationException`

### Features:
- JWT token validation
- Authentication filter
- Custom user loading
- Role-based structure groundwork

---

## ✅ 5. Controllers Implemented
### **AuthController** (`/auth/login`)
Uses:
- `AuthenticationManager`
- `CustomUserDetailsService`
- `JwtUtils`

Returns:
`ApiResponse<LoginResponse>`

---

### **UniversityAuthController** (`/auth/university/signup`)
Uses:
- `UniversityAuthService`

Returns:
`ApiResponse<UniversitySignupResponse>`

---

## ✅ 6. Service Layer Implemented
### **Login Service Logic**
- Authenticates user credentials
- Loads user details
- Generates JWT token

### **University Signup Service**
- Accepts signup request
- Validates email uniqueness
- Saves university
- Returns newly created university info

---


