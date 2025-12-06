package com.main.jobilitybackend.dto.requestDTO.rcnv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {
    private String title;
    private String detail;
    private String date;
    private String clubId;
    private String year;
    private String districtName;
    private String destrictNo;

    private String presidentName;
    private String presidentContact;

    private long cost;
    private long beneficiaries;
    private long manHours;
    private long rotarians;
    private long rotaractors;

    private String facebookLink;
    private String email;
    private String instaLink;

    private long clubProjectId;
    private long categoryId;
    private long subCategoryId;
}
