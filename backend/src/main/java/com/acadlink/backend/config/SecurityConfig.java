package com.acadlink.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for simplicity (especially for APIs)
            .csrf(csrf -> csrf.disable())
            
            // Authorize requests
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/health/**").permitAll()  // ✅ public health routes
                .anyRequest().authenticated()               // everything else is secured
            )
            
            // Disable form login, basic auth etc.
            .httpBasic(httpBasic -> httpBasic.disable())
            .formLogin(form -> form.disable());

        return http.build();
    }
}
