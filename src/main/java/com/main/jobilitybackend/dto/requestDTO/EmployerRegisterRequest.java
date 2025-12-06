package com.main.jobilitybackend.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerRegisterRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String designation;
    private String password;
}
