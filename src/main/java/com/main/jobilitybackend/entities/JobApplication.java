package com.main.jobilitybackend.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.jobilitybackend.enumConst.JobApplicationStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnore
    private Student applicant;

    @ManyToOne
    @JoinColumn(name = "job_post_id")
    private JobPost job;

    @ManyToOne
    @JoinColumn(name = "internship_post_id")
    private InternshipPost internship;

    @Enumerated(EnumType.STRING)
    private JobApplicationStatus status;
    
    @CreationTimestamp
    private LocalDateTime appliedAt;

    private LocalDateTime deleteAt;
}
