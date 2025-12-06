package com.main.jobilitybackend.services.serviceInterface;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.dto.requestDTO.rcnv.ProjectRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;

public interface ProjectService {
    DataResponse createProject(ProjectRequest project,List<MultipartFile> images);
    DataResponse getAllProjects(Long clubProjectId,Long categoryId,Long SubCategoryId,String city,String year,int page);
    DataResponse getProjectById(Long id);
    SuccessResponse updateProject(Long id, ProjectRequest project,List<MultipartFile> images);
    SuccessResponse deleteProject(Long id);
    DataResponse addProjectImages(long id,List<MultipartFile> images);
    SuccessResponse softDeleteProject(long id);
}
