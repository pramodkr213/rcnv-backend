package com.main.jobilitybackend.dto.responseDTO;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicProjectResponse {

	 private Long id;
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
	    private List<String> imageUrls;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;

	    private Long subCategoryId;
	    private Long categoryId;
	    private Long clubProjectId;
}
