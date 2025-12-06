package com.main.jobilitybackend.jwtSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.main.jobilitybackend.entities.Student;
import com.main.jobilitybackend.enumConst.Role;
import com.main.jobilitybackend.repositories.StudentRepo;


@Component
public class CustomStudentDetail implements UserDetailsService {

    @Autowired
    private StudentRepo studentRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = this.studentRepo.findByEmail(username).orElse(null);
        if (student == null) {
            throw new BadCredentialsException("User Not found");
        }
        return org.springframework.security.core.userdetails.User.builder().username(student.getUsername())
                .password(student.getPassword())
                .roles(Role.STUDENT.toString())
                .authorities(student.getAuthorities()).build();
    }

}
