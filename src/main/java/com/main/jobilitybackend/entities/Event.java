package com.main.jobilitybackend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
   
    private String description;

    private LocalDate date; // Event date

    // Getters and Setters
}

