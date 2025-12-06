package com.main.jobilitybackend.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.main.jobilitybackend.entities.Admin;
import com.main.jobilitybackend.entities.ClubProject;
import com.main.jobilitybackend.repositories.AdminRepo;
import com.main.jobilitybackend.repositories.ClubProjectRepo;

@Component
public class DefaultUserInitializer implements CommandLineRunner{

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private ClubProjectRepo clubProjectRepo;


    @Override
    public void run(String... args) throws Exception {
        if(!adminRepo.existsByDeleteAtIsNull()) {
            Admin admin = new Admin();
            admin.setEmail("mayur@gmail.com");
            admin.setPassword(new BCryptPasswordEncoder().encode("mayur@123"));
            this.adminRepo.save(admin);
            Admin admin1 = new Admin(); 
            admin1.setEmail("rahul@gmail.com");
            admin1.setPassword(new BCryptPasswordEncoder().encode("rahul@123"));
            this.adminRepo.save(admin1);
        }
        if (!clubProjectRepo.existsByDeleteAtIsNull()) {
            List<ClubProject> clubProjects = new ArrayList<>();
        
            clubProjects.add(new ClubProject("community services"));
            clubProjects.add(new ClubProject("district projects"));
            clubProjects.add(new ClubProject("club project"));
            clubProjects.add(new ClubProject("vocational services"));
            clubProjects.add(new ClubProject("international services"));
            clubProjects.add(new ClubProject("public image initiatives"));
        
            this.clubProjectRepo.saveAll(clubProjects);
        }
    }
    
}