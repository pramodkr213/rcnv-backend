package com.main.jobilitybackend.jwtSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.main.jobilitybackend.entities.Employer;
import com.main.jobilitybackend.enumConst.Role;
import com.main.jobilitybackend.repositories.EmployerRepo;

@Component
public class CustomEmployerDetail implements UserDetailsService{
    @Autowired
    private EmployerRepo employerRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employer employer = this.employerRepo.findByEmailAndDeleteAtIsNull(username).orElse(null);
        if (employer == null) {
            throw new BadCredentialsException("User Not found");
        }
        return org.springframework.security.core.userdetails.User.builder().username(employer.getUsername())
                .password(employer.getPassword())
                .roles(Role.EMPLOYER.toString())
                .authorities(employer.getAuthorities()).build();
    }
}
