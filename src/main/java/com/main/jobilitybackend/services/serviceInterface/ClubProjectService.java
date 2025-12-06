package com.main.jobilitybackend.services.serviceInterface;


import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.entities.ClubProject;

public interface ClubProjectService {
    DataResponse createClubProject(String name);
    DataResponse getAllClubProjects();
    DataResponse getClubProjectById(Long id);
    SuccessResponse updateClubProject(Long id, ClubProject clubProject);
    SuccessResponse deleteClubProject(Long id);
} 