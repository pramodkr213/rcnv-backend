package com.main.jobilitybackend.dto.responses.studentResponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStudentEmail {
    private String oldEmail;
    private String newEmail;
    private String password;
}
