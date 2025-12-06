package com.main.jobilitybackend.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.entities.Clubmembers;

public interface MembersService {

	

	List<Clubmembers> createMembers(List<Clubmembers> members, List<MultipartFile> images);

	Clubmembers updateMemberById(Long id, Clubmembers updatedData, MultipartFile image);

	ResponseEntity<?> deleteMember(Long id);

	Map<String, Object> findAllMembers(int page, int size);

	ResponseEntity<?> findMemberById(Long id);

	List<Clubmembers> findByMembersWithTodayDobOrAnniversary();

}
