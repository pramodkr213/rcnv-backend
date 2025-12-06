package com.main.jobilitybackend.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="job_post")
public class JobPost {

    @Id
    private String id;

    @Column(name = "title")
    private String title; 
    
    @Column(name="description",length = 5000)
    private String description; 

    @Column(name="is_fresher")
    private boolean isFresher;
    @Column(name = "min_experience")
    private int minExperience;
    @Column(name = "max_experience")
    private int maxExperience;

    @Column(name = "location")
    private String location;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "mode")
    private String mode;

    @Column(length = 5000)
    private String skills;

    @Column(name = "min_salary")
    private Long minSalary;
    @Column(name = "max_salary")
    private Long maxSalary;
    @Column(name = "num_of_working_days")
    private int numOfWorkingDays;

    @Column(name="other_salary_details",length = 1000)
    private String otherSalaryDetails;

    @Column(name = "number_of_vacancies")
    private int numberOfVacancies;

    @Column(name = "posted_by")
    private String postedBy;

    private boolean active;

    @Column(name = "last_date")
    private String lastDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deleteAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    @JsonIgnore 
    private Employer employer;

    @OneToMany(mappedBy = "job")
    @JsonIgnore
    private List<JobApplication> jobApplications;

    @OneToMany(mappedBy = "job")
    @JsonIgnore
    private List<JobBookmarks> jobBookmarks;

    //new parameter add
    private String sector;

    
    
}

