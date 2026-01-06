AcadLink_Professor_Student_Auth_Progress.md

Professor & Student Authentication (Signup)
✅ 1. Professor & Student Authentication Modules Added

#We implemented separate authentication modules for Professor and Student roles to ensure clear responsibility separation and scalability.
-Each module follows a layered architecture:

Controller
Service
DTOs (Request & Response)

✅ 2. Controller Layer Implemented
--#ProfessorAuthController
Base Path: /auth/professor
Endpoint Added:

"POST /auth/professor/signup"

#Responsibilities:

-Accept professor signup request
-Accept universityId as request parameter
-Delegate signup logic to service layer
-Handle validation and internal exceptions
-Return standardized ApiResponse

Response Type: ApiResponse<ProfessorSignupResponse>


--#StudentAuthController
Base Path: /auth/student
Endpoint Added:

POST /auth/student/signup
#Responsibilities:

-Accept student signup request
-Accept universityId as request parameter
-Call student signup service
-Handle validation and server exceptions
-Return standardized ApiResponse

Response Type: ApiResponse<StudentSignupResponse>

✅ 3. Service Layer Implemented
--#ProfessorAuthService
Handles complete business logic for professor signup.

#Responsibilities:
-Validate signup request
-Ensure professor email uniqueness
-Validate university existence
-Encrypt password before persistence
-Associate professor with university
-Persist professor entity
-Return response DTO

--#StudentAuthService
Handles complete business logic for student signup.

#Responsibilities:
-Validate signup request
-Ensure student email uniqueness
-Validate university existence
-Encrypt password
Associate student with university
-Persist student entity
-Return response DTO

✅ 4. DTOs Implemented (Request & Response)
#Professor DTOs
ProfessorSignupRequest
ProfessorSignupResponse

#Used for:
-Input validation
-Preventing entity exposure
-Clean API contracts

#Student DTOs
StudentSignupRequest
StudentSignupResponse

#Used for:
-Controlled request handling
-Clean response mapping
-Avoiding direct entity leakage

✅ 5. API Working Flow (Signup)
1️⃣ Client Request

Sends POST request to signup endpoint

#Includes:
Signup details in request body
universityId as request parameter

2️⃣ Controller Layer

Maps JSON request to SignupRequest DTO

Calls corresponding service:
ProfessorAuthService.signup()
StudentAuthService.signup()

3️⃣ Service Layer

Validates request data

Checks for existing email

Confirms university existence

Encrypts password

Saves user entity

Builds response DTO

4️⃣ Controller Response Handling

201 CREATED → Signup successful

400 BAD REQUEST → Validation errors

500 INTERNAL SERVER ERROR → Unexpected failures

✅ 6. Error Handling Strategy

-All exceptions are mapped to standardized error responses using ApiResponse.
-Validation Errors
-Duplicate email
-Invalid university ID
-HTTP Status: 400 BAD REQUEST

#Error Codes:

-PROFESSOR_SIGNUP_VALIDATION_ERROR
-STUDENT_SIGNUP_VALIDATION_ERROR
-Internal Server Errors
-Unexpected runtime failures
-HTTP Status: 500 INTERNAL SERVER ERROR

#Error Codes:
PROFESSOR_SIGNUP_INTERNAL_ERROR
STUDENT_SIGNUP_INTERNAL_ERROR

✅ 7. Consistency with Core Architecture

#The Professor & Student signup modules align perfectly with:

-Global API response structure
-Exception handling strategy
-JWT-based security foundation
-Clean separation of concerns

