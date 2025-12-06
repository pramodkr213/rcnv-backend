package com.main.jobilitybackend.entities;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.jobilitybackend.enumConst.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employer")
public class Employer implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phone;
    private String designation;
    private String role = "EMPLOYER";

    @Column(name = "company_name")
    private String companyName;
    private String discription;
    private String city;
    private String industryType;
    private String noEmployees;
    private String logoUrl;
    private String documentUrl;
    private String website;
    private String smLink;
    private boolean isDocument=true;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private Instant deleteAt;

    private boolean isEmailVerified = false;
    private boolean verified = false;
    private boolean active = true;

    @OneToMany(mappedBy = "employer")
    @JsonIgnore
    private List<JobPost> jobPosts;

    @OneToMany(mappedBy = "employer")
    @JsonIgnore
    private List<InternshipPost> intershipPosts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+Role.EMPLOYER.toString()));
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }
}
