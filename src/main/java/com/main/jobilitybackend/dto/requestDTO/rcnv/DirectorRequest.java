package com.main.jobilitybackend.dto.requestDTO.rcnv;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectorRequest {
    private String name;
    private String email;
    private String designation;
    private LocalDate startDate;
    private LocalDate endDate;
}
