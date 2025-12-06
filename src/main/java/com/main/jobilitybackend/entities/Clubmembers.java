package com.main.jobilitybackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Clubmembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private Long mobile;
  
    private String classification;
    private String memberdob;
    private String spousename;
    private String spousedob;
    private String memberaniversary;
    private String description;
    private String imgurl;
}

