package com.main.jobilitybackend.dto.requestDTO;



import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateEventRequest {
    private String title;
    private LocalDate date;
}
