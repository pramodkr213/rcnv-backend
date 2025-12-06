package com.main.jobilitybackend.services.serviceInterface;
import com.main.jobilitybackend.dto.requestDTO.AdminRegisterRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;

public interface AdminService {
    DataResponse addAdmin(AdminRegisterRequest request)throws Exception;
    String adminLogin(String email, String password)throws Exception;
    DataResponse getAdminById(Long id)throws Exception;
    DataResponse getAdminByEmail(String email)throws Exception;

    
    DataResponse getAdminDashboard()throws Exception;
}
