package com.acadlink.backend.security;

import com.acadlink.backend.security.model.Role;
import com.acadlink.backend.security.model.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // TODO: Replace with actual UserRepository (DB lookup)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (!email.equals("admin@acadlink.com")) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Mock user
        return new UserPrincipal("1", "admin@acadlink.com", "password", Role.SUPER_ADMIN);
    }
}
