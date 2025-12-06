package com.main.jobilitybackend.jwtSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.main.jobilitybackend.entities.Admin;
import com.main.jobilitybackend.enumConst.Role;
import com.main.jobilitybackend.repositories.AdminRepo;


@Service
public class CustomAdminDetail implements UserDetailsService{

    @Autowired
    private AdminRepo adminRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = this.adminRepo.findByEmail(username).orElse(null);
        if (admin == null) {
            throw new BadCredentialsException("User Not found");
        }
        return org.springframework.security.core.userdetails.User.builder().username(admin.getUsername())
                .password(admin.getPassword())
                .roles(Role.ADMIN.toString())
                .authorities(admin.getAuthorities()).build();
    }
}
