package com.acadlink.backend.university.controller;

import com.acadlink.backend.common.ApiResponse;
import com.acadlink.backend.common.ErrorCode;
import com.acadlink.backend.university.dto.UniversitySignupRequest;
import com.acadlink.backend.university.dto.UniversitySignupResponse;
import com.acadlink.backend.university.service.UniversityAuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/university")
public class UniversityAuthController {

    private final UniversityAuthService universityAuthService;

    public UniversityAuthController(UniversityAuthService universityAuthService) {
        this.universityAuthService = universityAuthService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UniversitySignupResponse>> signup(
            @Valid @RequestBody UniversitySignupRequest request) {

        try {
            UniversitySignupResponse response = universityAuthService.signup(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(
                            "University registered successfully",
                            response
                    ));

        } catch (IllegalArgumentException ex) {

            //  Example: university email already exists
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(
                            ex.getMessage(),
                            ErrorCode.UNIVERSITY_SIGNUP_VALIDATION_ERROR
                    ));

        } catch (Exception ex) {

            //  Any unexpected server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Something went wrong while creating university",
                            ErrorCode.UNIVERSITY_SIGNUP_INTERNAL_ERROR
                    ));
        }
    }
}
