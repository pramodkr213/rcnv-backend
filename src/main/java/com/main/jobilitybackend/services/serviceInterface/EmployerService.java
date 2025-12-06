
package com.main.jobilitybackend.services.serviceInterface;

import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.dto.requestDTO.CompanyVarificationRequest;
import com.main.jobilitybackend.dto.requestDTO.EmployerRegisterRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;

public interface EmployerService {
    DataResponse addEmployer(EmployerRegisterRequest request) throws Exception;
    String employerLogin(String email, String password) throws Exception;
    DataResponse getEmployerById(Long id) throws Exception;
    DataResponse getEmployerByEmail(String email) throws Exception;
    DataResponse addCompanyVarification(CompanyVarificationRequest request,String email,MultipartFile logo,MultipartFile document) throws Exception;
    DataResponse getPendingEmployerRequest(int page,String keywords,String companyName,String city,String industryType) throws Exception;
    DataResponse getEmployers(int page,String keywords,String companyName,String city,String industryType,String date) throws Exception;
    DataResponse getEmployerByIdForAdmin(Long id) throws Exception;

    DataResponse getEmployerDashboard(String jwt) throws Exception;
    SuccessResponse varifyEmployer(Long id) throws Exception;
    SuccessResponse activeAndIncativeEmployer(Long id) throws Exception;
    SuccessResponse deleteEmployer(Long id) throws Exception;
    
}
