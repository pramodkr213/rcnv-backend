package com.main.jobilitybackend.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRegisterRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

}
