package com.main.jobilitybackend.services.serviceInterface;

import com.main.jobilitybackend.dto.responseDTO.DataResponse;

public interface UserService {
    DataResponse getProfile(String token) throws Exception;
}
