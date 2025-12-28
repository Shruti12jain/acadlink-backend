package com.acadlink.backend.security.controller;

import com.acadlink.backend.security.dto.LoginRequest;
import com.acadlink.backend.security.dto.LoginResponse;
import com.acadlink.backend.security.model.UserPrincipal;
import com.acadlink.backend.security.service.CustomUserDetailsService;
import com.acadlink.backend.utils.JwtUtils;
import com.acadlink.backend.common.ApiResponse;
import com.acadlink.backend.common.ErrorCode;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            CustomUserDetailsService userDetailsService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(
                            "Invalid email or password",
                            ErrorCode.AUTH_INVALID_CREDENTIALS
                    ));
        }

        UserPrincipal user = (UserPrincipal)
                userDetailsService.loadUserByUsername(request.email());

        String token = jwtUtils.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );

        LoginResponse loginResponse = new LoginResponse(
                token,
                user.getRole().name(),
                user.getId()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Login successful",
                        loginResponse
                )
        );
    }
}
