package com.main.jobilitybackend.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyVarificationRequest {
    private String companyName;
    private String discription;
    private String city;
    private String industryType;
    private String noEmployees;
    private String website;
    private String smLink;
    private boolean isDocument=true;
}
