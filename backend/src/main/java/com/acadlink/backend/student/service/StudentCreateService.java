package com.acadlink.backend.student.service;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.acadlink.backend.professor.repository.ProfessorRepository;
import com.acadlink.backend.student.dto.BulkUploadError;
import com.acadlink.backend.student.dto.BulkUploadResponse;
import com.acadlink.backend.student.dto.StudentSignupRequest;
import com.acadlink.backend.student.dto.StudentSignupResponse;

import  com.acadlink.backend.student.repository.StudentRepository;
import com.acadlink.backend.university.repository.UniversityRepository;
import com.acadlink.backend.utils.EmailService;
import com.acadlink.backend.utils.ExcelValidator;
import com.acadlink.backend.utils.JwtUtils;
import com.acadlink.backend.utils.PasswordGenerator;

import java.io.IOException;
import java.util.UUID;
import com.acadlink.backend.student.entity.Student;
import com.acadlink.backend.university.entity.University;
import com.acadlink.backend.professor.entity.Professor;

@Service
public class StudentCreateService {
    private final UniversityRepository UniversityRepository;
    private final ProfessorRepository ProfessorRepository;
    private final JwtUtils jwtUtils;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ExcelValidator excelValidator;


    public StudentCreateService(
         JwtUtils jwtUtils,
         StudentRepository studentRepository, 
         PasswordEncoder passwordEncoder, 
         UniversityRepository universityRepository,
         ProfessorRepository professorRepository, 
         EmailService emailService,
         ExcelValidator excelValidator) {
        this.jwtUtils = jwtUtils;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.UniversityRepository = universityRepository;
        this.ProfessorRepository = professorRepository;
        this.emailService = emailService;
        this.excelValidator=excelValidator;
    }

   public StudentSignupResponse createuniversity(StudentSignupRequest request, UUID universityId) {
    // check if university exists because student can only be registered by university and professor 
    if (!UniversityRepository.existsById(universityId)) {
        throw new IllegalArgumentException("University does not exist");
    }
    // check if student already exists 
    if (studentRepository.existsByEmail(request.email())) {
        throw new IllegalArgumentException("Email already registered");
    }
     // create new student
    Student student = new Student();
    student.setName(request.name());
    String password;
    if(request.password()==null){
       PasswordGenerator passwordObj= new PasswordGenerator();
       password =passwordObj.generatePassword(8);
       student.setPassword(passwordEncoder.encode(password));
       
    }else {
         password=request.password();
         student.setPassword(passwordEncoder.encode(request.password()));
    }
    

    
    student.setEmail(request.email());
    // save student
    Student saved = studentRepository.save(student);
    emailService.sendEmail(request.email(), "Acadlink Password ", "Your password is "+ password);
    // associate student with university
    University university = UniversityRepository.findById(universityId)
            .orElseThrow(() -> new IllegalArgumentException("University not found"));

    university.getStudents().add(saved);
    UniversityRepository.save(university);

    return new StudentSignupResponse(
            saved.getId().toString(),
            saved.getEmail()
    );
  }

   public StudentSignupResponse createprofessor(StudentSignupRequest request, UUID professorId) {
    // check if university exists because student can only be registered by university and professor 
    
    // check if student already exists 
    if (studentRepository.existsByEmail(request.email())) {
        throw new IllegalArgumentException("Email already registered");
    }
    // check if professor exists because student can only be registered by university and professor 
    if (!ProfessorRepository.existsById(professorId)) {
        throw new IllegalArgumentException("professor does not exist");
    }
     // create new student
    Student student = new Student();
    student.setName(request.name());
    String password;
    if(request.password()==null){
       PasswordGenerator passwordObj= new PasswordGenerator();
       password =passwordObj.generatePassword(8);
       student.setPassword(passwordEncoder.encode(password));
       
    }else {
        password=request.password();
         student.setPassword(passwordEncoder.encode(request.password()));
    }
    
    student.setEmail(request.email());
    // save student
    Student saved = studentRepository.save(student);
   
    emailService.sendEmail(request.email(), "Acadlink Password ", "Your password is "+ password);
    //associate student with professor 
    Professor professor = ProfessorRepository.findById(professorId)
            .orElseThrow(() -> new IllegalArgumentException("Professor not found"));
    
    professor.getStudents().add(saved);
    ProfessorRepository.save(professor);        

    // associate student with university
    UUID universityId = professor.getUniversity().getId();
    University university = UniversityRepository.findById(universityId)
            .orElseThrow(() -> new IllegalArgumentException("University not found"));

    university.getStudents().add(saved);
    UniversityRepository.save(university);

    
    return new StudentSignupResponse(
            saved.getId().toString(),
            saved.getEmail()
    );
  }


   public BulkUploadResponse uploadUnderUniversity(MultipartFile file, UUID universityId) {

    // Validate university
    if (!UniversityRepository.existsById(universityId)) {
        throw new IllegalArgumentException("University does not exist");
    }

    excelValidator.validateFile(file);

    BulkUploadResponse response = new BulkUploadResponse();

    try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

        Sheet sheet = workbook.getSheetAt(0);

        response.setTotalRows(sheet.getLastRowNum());

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) {
                response.setSkipped(response.getSkipped() + 1);
                continue;
            }

            try {
                String name = row.getCell(0).getStringCellValue().trim();
                String email = row.getCell(1).getStringCellValue().trim();

                // Row validation
                if (name.isEmpty() || email.isEmpty()) {
                    response.setSkipped(response.getSkipped() + 1);
                    continue;
                }

                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    BulkUploadError error=new BulkUploadError(i + 1, email, "Invalid email format");
                    response.getErrors().add(error);
                    response.setFailed(response.getFailed() + 1);
                    continue;
                }

                if (studentRepository.existsByEmail(email)) {
                    response.setSkipped(response.getSkipped() + 1);
                    continue;
                }

                // Generate password
                PasswordGenerator passwordObj = new PasswordGenerator();
                String rawPassword = passwordObj.generatePassword(8);

                // Save student
                Student student = new Student();
                student.setEmail(email);
                student.setPassword(passwordEncoder.encode(rawPassword));
                student.setName(name);

                Student saved = studentRepository.save(student);

                // Associate with university
                University university = UniversityRepository.findById(universityId)
                        .orElseThrow(() -> new IllegalArgumentException("University not found"));

                university.getStudents().add(saved);
                UniversityRepository.save(university);

                // Send email
                emailService.sendEmail(
                        email,
                        "Acadlink Password",
                        "Your password is " + rawPassword
                );

                response.setSuccessful(response.getSuccessful() + 1);

            } catch (Exception ex) {
                response.getErrors().add(
                        new BulkUploadError(i + 1, "UNKNOWN", ex.getMessage())
                );
                response.setFailed(response.getFailed() + 1);
            }
        }

    } catch (IOException e) {
        throw new RuntimeException("Failed to process Excel file", e);
    }

    return response;
}

   public void uploadUnderProfessor(MultipartFile file,UUID professorId){
    
   }
}