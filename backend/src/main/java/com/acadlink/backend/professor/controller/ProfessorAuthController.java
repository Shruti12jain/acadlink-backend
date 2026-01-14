package com.acadlink.backend.professor.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.acadlink.backend.professor.service.ProfessorAuthService;

import jakarta.validation.Valid;

import com.acadlink.backend.professor.dto.ProfessorSignupRequest;
import com.acadlink.backend.professor.dto.ProfessorSignupResponse;
import com.acadlink.backend.common.ApiResponse;
import com.acadlink.backend.common.ErrorCode;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.UUID;
import org.springframework.http.HttpStatus;



@RestController
@RequestMapping("auth/professor")
public class ProfessorAuthController {
    private final ProfessorAuthService professorauthservice;

    public ProfessorAuthController(ProfessorAuthService professorauthservice){
        this.professorauthservice = professorauthservice;
   } 
   @PostMapping("/signup")
   public ResponseEntity<ApiResponse<ProfessorSignupResponse>> signup(
           @Valid @RequestBody ProfessorSignupRequest request,
           @RequestParam UUID universityId
   )
{
     try{
        ProfessorSignupResponse response = professorauthservice.signup(request, universityId);
        return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(
                            "Professor registered successfully",
                            response
                    ));
     }catch(IllegalArgumentException ex){
         //  Example: professor email already exists or university does not exist
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(
                            ex.getMessage(),
                            ErrorCode.PROFESSOR_SIGNUP_VALIDATION_ERROR
                    ));

     }catch(Exception ex){
         //  Any unexpected server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Something went wrong while creating professor",
                            ErrorCode.Professor_SIGNUP_INTERNAL_ERROR
                    ));

     }
}
}
