# AcadLink – Student Creation under Professor & University (Progress 4)

This document tracks the **implementation progress of Student Creation APIs**, where students are created using the **existing Student Signup DTOs**, with validations enforced at the DTO level.

Student creation is supported under:
1. **University**
2. **Professor**

---

## ✅ 1. Reused DTOs with Validation

### 📦 DTOs Used

The following DTOs are reused for both student creation flows:

- `StudentSignupRequest`
- `StudentSignupResponse`

### 🛡️ DTO-Level Validations

Validations are enforced using **Bean Validation annotations**, ensuring request correctness before reaching the service layer.

- `@NotBlank` – name, email, password
- `@Email` – email format validation
- `@Size` – password strength constraints

DTO validation failures are automatically handled by the global exception handler and returned in standardized responses.

---

## ✅ 2. Student Creation under University

### 📌 Overview

Allows a **University** to create a student and associate the student directly with itself, without linking the student to any professor at creation time.

---

### 🔗 API Details

**Base Path**
/students

**Endpoint**

---

### 🧾 Request Handling

- Accepts student details in request body
- Accepts `universityId` as request parameter
- Uses `StudentSignupRequest` DTO
- DTO validations are triggered automatically

---

### ⚙️ Service Responsibilities

Handled by `StudentService`:

- Validate university existence
- Ensure student email uniqueness
- Encrypt password
- Associate student with university
- Persist student entity
- Build `StudentSignupResponse`

---

### 📦 Response

**Success**
- HTTP Status: `201 CREATED`
- Returns: `ApiResponse<StudentSignupResponse>`

**Failure**
- HTTP Status: `400 BAD REQUEST`
- Invalid university ID
- Duplicate student email
- DTO validation failures

---

## ✅ 3. Student Creation under Professor

### 📌 Overview

Allows a **Professor** to create a student.  
The student is automatically associated with:
- The professor
- The professor’s university

---

### 🔗 API Details

**Base Path**
POST /students/create/professor

---

### 🧾 Request Handling

- Accepts student details in request body
- Accepts `professorId` as request parameter
- Uses `StudentSignupRequest` DTO
- DTO validations are enforced before service execution

---

### ⚙️ Service Responsibilities

Handled by `StudentCreateService`:

- Validate professor existence
- Resolve professor → university mapping
- Ensure student email uniqueness
- Encrypt password
- Associate student with professor
- Associate student with professor’s university
- Persist student entity
- Build `StudentSignupResponse`

---

### 📦 Response

**Success**
- HTTP Status: `201 CREATED`
- Returns: `ApiResponse<StudentSignupResponse>`

**Failure**
- HTTP Status: `400 BAD REQUEST`
- Invalid professor ID
- Duplicate student email
- DTO validation failures

---

## ✅ 4. Error Handling Strategy

All errors follow the standardized `ApiResponse` structure.

### Validation Errors

- DTO validation failures
- Duplicate student email
- Invalid university ID
- Invalid professor ID

**HTTP Status:** `400 BAD REQUEST`

**Error Codes**
- `STUDENT_SIGNUP_UNIVERSITY_VALIDATION_ERROR`
- `STUDENT_SIGNUP_PROFESSOR_VALIDATION_ERROR`

---

### Internal Errors

**HTTP Status:** `500 INTERNAL SERVER ERROR`

**Error Codes**
- `STUDENT_SIGNUP_UNIVERSITY_INTERNAL_ERROR`
- `STUDENT_SIGNUP_PROFESSOR_INTERNAL_ERROR`

---

## ✅ 5. Architectural Consistency

Student creation APIs are fully aligned with:

- Reusable DTO-driven architecture
- DTO-level validation strategy
- Global API response structure
- Centralized exception handling
- JWT-based security foundation
- Core entity relationships

---

## 📌 Current Status

- Student creation under **University** → ✅ Implemented & Stable  
- Student creation under **Professor** → ✅ Implemented & Stable  
- DTO reuse & validation → ✅ Completed  
- Error handling → ✅ Verified  

---

📘 **Conclusion:**  
Student creation flows under both University and Professor contexts are complete, robust, and consistent with the existing signup architecture, ensuring scalability and maintainability.
