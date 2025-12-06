package com.main.jobilitybackend.dto.responses.studentResponses;

import java.util.List;

public interface StudentProfileResponse extends StudentResponse{
    List<EducationInfo> getEducation();

    interface EducationInfo {
        String getId();
        String getDegree();
        String getType();
        String getCollege();
        String getFieldOfStudy();
        String getYearOfPassing();
    }
}
