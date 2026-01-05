package com.acadlink.backend.student.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.acadlink.backend.student.service.StudentAuthService;
import com.acadlink.backend.common.ApiResponse;
import com.acadlink.backend.common.ErrorCode;
import com.acadlink.backend.student.dto.StudentSignupRequest;
import com.acadlink.backend.student.dto.StudentSignupResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.UUID;


@RestController
@RequestMapping("/auth/student")
public class StudentAuthController {

    private final StudentAuthService studentAuthService;

    public StudentAuthController(StudentAuthService studentAuthService) {
        this.studentAuthService = studentAuthService;
    }
    

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<StudentSignupResponse>> signup(
            @RequestBody StudentSignupRequest request,
            @RequestParam UUID universityId
    )
{    
        try {
        
            StudentSignupResponse response = studentAuthService.signup(request, universityId);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(
                            "Student registered successfully",
                            response
                    ));

        } catch (IllegalArgumentException ex) {

            //  Example: student email already exists or university does not exist
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(
                            ex.getMessage(),
                            ErrorCode.STUDENT_SIGNUP_VALIDATION_ERROR
                    ));

        } catch (Exception ex) {

            //  Any unexpected server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Something went wrong while creating student",
                            ErrorCode.STUDENT_SIGNUP_INTERNAL_ERROR
                    ));
        }
}
}

