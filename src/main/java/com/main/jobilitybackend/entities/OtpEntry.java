package com.main.jobilitybackend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OtpEntry {
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String email;
    private String otp;
    private LocalDateTime expirationTime;
    private LocalDateTime sessionTime;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
    }
    
    public boolean isSessionExpired() {
        return LocalDateTime.now().isAfter(sessionTime);
    }
}
