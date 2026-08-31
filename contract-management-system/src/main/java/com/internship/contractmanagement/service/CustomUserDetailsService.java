package com.internship.contractmanagement.service;

import com.internship.contractmanagement.entity.Role;
import com.internship.contractmanagement.entity.User;
import com.internship.contractmanagement.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security doesn't know about OUR "User" entity - it works with its
 * own "UserDetails" interface. This class is the translator between the two:
 * "given an email, go find our User in the database, and hand back the
 * shape Spring Security actually understands."
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));

        // Convert our Set<Role> into what Spring Security expects.
        // Spring Security's convention: role names must be prefixed "ROLE_"
        // e.g. our Role "ADMIN" becomes the authority "ROLE_ADMIN"
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(Role::getName)
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword()) // already BCrypt-hashed, never plain text
                .authorities(authorities)
                .build();
    }
}