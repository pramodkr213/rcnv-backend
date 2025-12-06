package com.main.jobilitybackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "internship_post")
public class InternshipPost {

    @Id
    private String id;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;


    private String duration;

    private boolean isPaid;
    private String minStipend;
    private String maxStipend;
    private String mode;
    private String location;
    private String internshipType;

    private String eligibility;
    private String skillsRequired;

    private int numberOfOpenings;
    private boolean isImmediate;
    private LocalDate joinFrom;
    private LocalDate joinTo;

    private LocalDate applicationDeadline;

    @Column(length = 5000)
    private String internshipDescription;

    private String postedBy; // Email or Admin Name
    private boolean isActive;
  
    @CreationTimestamp
    @Column(name = "created_at" )
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "delete_at")
    private LocalDateTime deleteAt;


    @ManyToOne
    @JoinColumn(name = "employer_id")
    @JsonIgnore 
    private Employer employer;

    @OneToMany(mappedBy = "internship")
    @JsonIgnore
    private List<JobApplication> applications;

    @OneToMany(mappedBy = "internship")
    @JsonIgnore
    private List<JobBookmarks> jobBookmarks;
}


