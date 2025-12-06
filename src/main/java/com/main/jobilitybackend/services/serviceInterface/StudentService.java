package com.main.jobilitybackend.services.serviceInterface;
import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.dto.requestDTO.StudentRegisterRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.dto.responses.studentResponses.ChangeStudentEmail;
import com.main.jobilitybackend.dto.responses.studentResponses.StudentDashboardResponse;
import com.main.jobilitybackend.dto.responses.studentResponses.StudentDataResponse;
import com.main.jobilitybackend.dto.responses.studentResponses.StudentProfileUpdate;


public interface StudentService {
    DataResponse addStudent(StudentRegisterRequest request) throws Exception;
    String studentLogin(String email, String password,long expirationTime) throws Exception;
    DataResponse getStudentById(String id) throws Exception;
    DataResponse getStudentByEmail(String email) throws Exception;
    DataResponse changeEmailRequest(String email, String newEmail) throws Exception;
    DataResponse changeStudentEmail(ChangeStudentEmail request) throws Exception;
    DataResponse updateStudentProfile(String email, StudentProfileUpdate studentProfileUpdate) throws Exception;

    DataResponse getAllStudents(int page,String keywords,String city,String date,String degree,String college,String fieldOfStudy,String yearOfPassing) throws Exception;
    SuccessResponse activeAndInactiveStudent(String id) throws Exception;
    SuccessResponse deleteStudent(String id) throws Exception;

    SuccessResponse uploadImage(String email, MultipartFile file) throws Exception;
    SuccessResponse uploadResume(String email, MultipartFile file) throws Exception;
    SuccessResponse updateCareerObjectives(String email,String carrerObjective);
    SuccessResponse updateAboutMe(String email,String aboutMe);

    
    StudentDataResponse<StudentDashboardResponse> getStudentDashboard(String jwt) throws Exception;
  

}
