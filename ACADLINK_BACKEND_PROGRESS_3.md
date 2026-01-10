# AcadLink – Student Signup Progress Documentation

This document tracks the **implementation progress of Student Signup flows** in AcadLink, covering both:

1. **Student Signup under University**
2. **Student Signup under Professor**

It reflects the current backend status, architecture alignment, and completed responsibilities.

---

## ✅ 1. Student Signup under University

### 📌 Overview

This flow allows a student to directly register under a specific university without being immediately associated with a professor.

---

### 🔗 API Details

**Base Path**

```
/auth/student
```

**Endpoint**

```
POST /auth/student/signup/university
```

---

### 🧾 Request Handling

* Accepts student signup details in request body
* Accepts `universityId` as request parameter
* Maps request to `StudentSignupRequest` DTO

---

### ⚙️ Service Responsibilities

Handled by `StudentAuthService`:

* Validate signup request
* Ensure student email uniqueness
* Validate university existence
* Encrypt password using BCrypt
* Associate student with university
* Persist student entity
* Build `StudentSignupResponse` DTO

---

### 📦 Response

**Success**

* HTTP Status: `201 CREATED`
* Returns: `ApiResponse<StudentSignupResponse>`

**Failure**

* HTTP Status: `400 BAD REQUEST`
* Invalid university ID
* Duplicate email

---

## ✅ 2. Student Signup under Professor

### 📌 Overview

This flow allows a student to register under a specific professor. The student is implicitly linked to the professor and the professor’s university.

---

### 🔗 API Details

**Base Path**

```
/auth/student
```

**Endpoint**

```
POST /auth/student/signup/professor
```

---

### 🧾 Request Handling

* Accepts student signup details in request body
* Accepts `professorId` as request parameter
* Maps request to `StudentSignupRequest` DTO

---

### ⚙️ Service Responsibilities

Handled by `StudentAuthService`:

* Validate signup request
* Ensure student email uniqueness
* Validate professor existence
* Resolve professor → university mapping
* Encrypt password
* Associate student with professor
* Associate student with professor’s university
* Persist student entity
* Build `StudentSignupResponse` DTO

---

### 📦 Response

**Success**

* HTTP Status: `201 CREATED`
* Returns: `ApiResponse<StudentSignupResponse>`

**Failure**

* HTTP Status: `400 BAD REQUEST`
* Invalid professor ID
* Duplicate email

---

## ✅ 3. Error Handling Strategy

All errors are returned using the standardized `ApiResponse` format.

### Validation Errors

* Duplicate student email
* Invalid university ID
* Invalid professor ID

**HTTP Status:** `400 BAD REQUEST`

**Error Codes**

* `STUDENT_SIGNUP_UNIVERSITY_VALIDATION_ERROR`
* `STUDENT_SIGNUP_PROFESSOR_VALIDATION_ERROR`

---

### Internal Errors

**HTTP Status:** `500 INTERNAL SERVER ERROR`

**Error Codes**

* `STUDENT_SIGNUP_UNIVERSITY_INTERNAL_ERROR`
* `STUDENT_SIGNUP_PROFESSOR_INTERNAL_ERROR`

---

## ✅ 4. Architectural Consistency

Both student signup flows are fully aligned with:

* Core entity relationships
* Global API response structure
* Centralized exception handling
* JWT-based security foundation
* DTO-driven request/response model

---

## 📌 Current Status

* Student signup under **University** → ✅ Implemented & Stable
* Student signup under **Professor** → ✅ Implemented & Stable
* Validation & error handling → ✅ Complete
* Security compatibility → ✅ Verified

---

📘 **Conclusion:** Student signup workflows are complete, stable, and ready for further extensions such as approval workflows, email verification, and professor-based access control.
