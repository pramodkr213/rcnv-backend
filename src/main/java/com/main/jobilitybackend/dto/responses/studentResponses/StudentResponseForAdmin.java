package com.main.jobilitybackend.dto.responses.studentResponses;

import java.util.List;

public interface StudentResponseForAdmin extends StudentResponse {

    List<EducationInfo> getEducation();

    interface EducationInfo {
        String getId();
        String getDegree();
        String type();
        String getCollege();
        String getFieldOfStudy();
        String getYearOfPassing();
    }
    
    
}
