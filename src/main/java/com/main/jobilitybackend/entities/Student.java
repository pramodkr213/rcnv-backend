package com.main.jobilitybackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.jobilitybackend.enumConst.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student implements UserDetails {

    @Id
    private String id;

    // Basic details
    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String phone;
    private String gender;
    private LocalDate dob;
    private String lanuagesKnown;

    @Column(length = 5000)
    private String careerObjective;

    @Column(length = 2000)
    private String aboutMe;
    private String role = "STUDENT";

    // Skills and resume
    @Column(length = 1000)
    private String skills;

    private String resumeLink;
    private String profilePicture; 

    // Address info
    private String city;
    private String state;
    private String country;

    // Social links
    private String linkedinProfile;
    private String githubProfile;
    private String portfolio;

    // Security + Status
    private boolean active = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deleteAt;

    @OneToMany(mappedBy = "student")
    @JsonIgnore 
    private List<Education> education;

    @OneToMany(mappedBy = "applicant")
    private List<JobApplication> jobApplications;

    @OneToMany(mappedBy = "student")
    private List<JobBookmarks> jobBookmarks;

    //========== Spring Security Methods ==========

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + Role.STUDENT.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
