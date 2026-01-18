package com.acadlink.backend.student.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.acadlink.backend.common.ApiResponse;
import com.acadlink.backend.common.ErrorCode;
import com.acadlink.backend.student.dto.BulkUploadResponse;
import com.acadlink.backend.student.dto.StudentSignupRequest;
import com.acadlink.backend.student.dto.StudentSignupResponse;
import com.acadlink.backend.student.service.StudentCreateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/create/student")
public class Studentcreatecontroller {
     private final StudentCreateService studentCreateService;

    public Studentcreatecontroller(StudentCreateService studentCreateService) {
        this.studentCreateService = studentCreateService;
    }

    @PostMapping("/under-university")
    public ResponseEntity<ApiResponse<StudentSignupResponse>> createunderuniversity(
            @Valid @RequestBody StudentSignupRequest request,
            @RequestParam UUID universityId
    )
  {    
        try {
        
            StudentSignupResponse response = studentCreateService.createuniversity(request, universityId);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(
                            "Student created successfully",
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


 @PostMapping("/under-professor")
    public ResponseEntity<ApiResponse<StudentSignupResponse>> createunderprofessor(

            @Valid @RequestBody StudentSignupRequest request,
            @RequestParam UUID professorId
            
        
    )
  {    
        try {
        
            StudentSignupResponse response = studentCreateService.createprofessor(request, professorId);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(
                            "Student created successfully",
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


  @PostMapping("/upload/under-university")
  public  ResponseEntity<ApiResponse<BulkUploadResponse>> UploadUnderUnivesity( @RequestParam UUID universityId,@RequestParam("file") MultipartFile file) {
     
        try {
                BulkUploadResponse response = studentCreateService.uploadUnderUniversity(file, universityId);

                return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(
                            "Students created successfully",
                            response
                    ));
                
        } catch (IllegalArgumentException ex) {

            //  Example: student email already exists or university does not exist
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(
                            ex.getMessage(),
                            ErrorCode.STUDENT_SIGNUP_VALIDATION_ERROR
                    ));
                }
        catch (Exception e) {
             //  Any unexpected server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Something went wrong while creating student",
                            ErrorCode.STUDENT_SIGNUP_INTERNAL_ERROR
                    ));    
        }

  }
  
}
    

