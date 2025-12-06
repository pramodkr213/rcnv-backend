package com.main.jobilitybackend.services.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.entities.Clubmembers;
import com.main.jobilitybackend.helper.ImageUploader;
import com.main.jobilitybackend.repositories.MemberRepository;
import com.main.jobilitybackend.service.MembersService;
@Service
public class MembersServiceImpl  implements MembersService{
	@Autowired
	private MemberRepository memberRepository;


    @Autowired
    private ImageUploader imageUploader;

	@Override
	public List<Clubmembers> createMembers(List<Clubmembers> members, List<MultipartFile> images) {
		  List<Clubmembers> savedMembers = new ArrayList<>();

		    for (int i = 0; i < members.size(); i++) {
		        Clubmembers member = members.get(i);

		        if (i < images.size()) {
		            String imageUrl = imageUploader.imageUploader(images.get(i));
		            member.setImgurl(imageUrl);
		        }

		        savedMembers.add(memberRepository.save(member));
		    }

		    return savedMembers;
		}

	@Override
	public Clubmembers updateMemberById(Long id, Clubmembers updatedData, MultipartFile image) {
	    Optional<Clubmembers> optionalMember = memberRepository.findById(id);
	    if (!optionalMember.isPresent()) {
	        throw new RuntimeException("Member not found with id: " + id);
	    }

	    Clubmembers member = optionalMember.get();

	    // Update fields
	    member.setName(updatedData.getName() != null ? updatedData.getName() : member.getName());
	    member.setEmail(updatedData.getEmail() != null ? updatedData.getEmail() : member.getEmail());
	    member.setMobile(updatedData.getMobile() != null ? updatedData.getMobile() : member.getMobile());
	    member.setClassification(updatedData.getClassification() != null ? updatedData.getClassification() : member.getClassification());
	    member.setMemberdob(updatedData.getMemberdob() != null ? updatedData.getMemberdob() : member.getMemberdob());
	    member.setSpousename(updatedData.getSpousename() != null ? updatedData.getSpousename() : member.getSpousename());
	    member.setSpousedob(updatedData.getSpousedob() != null ? updatedData.getSpousedob() : member.getSpousedob());
	    member.setMemberaniversary(updatedData.getMemberaniversary() != null ? updatedData.getMemberaniversary() : member.getMemberaniversary());
	    member.setDescription(updatedData.getDescription() != null ? updatedData.getDescription() : member.getDescription());


	    if (image != null && !image.isEmpty()) {
	        String imageUrl = imageUploader.imageUploader(image);
	        member.setImgurl(imageUrl);
	    }

	    return memberRepository.save(member);
	}

	@Override
	public ResponseEntity<String> deleteMember(Long id) {
	    Optional<Clubmembers> member = memberRepository.findById(id);

	    if (member.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
	    }

	    memberRepository.deleteById(id);
	    return ResponseEntity.ok("Member deleted successfully");
	}

	@Override
	public Map<String, Object> findAllMembers(int page, int size) {
	    Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
	    Page<Clubmembers> membersPage = memberRepository.findAll(pageable);

	    Map<String, Object> response = new HashMap<>();
	    response.put("members", membersPage.getContent());
	    response.put("currentPage", membersPage.getNumber());
	    response.put("totalItems", membersPage.getTotalElements());
	    response.put("totalPages", membersPage.getTotalPages());

	    return response;
	}


	@Override
	public ResponseEntity<?> findMemberById(Long id) {
		Optional<Clubmembers> member = memberRepository.findById(id);

	    if (member.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
	    }
	Optional<Clubmembers> members =     memberRepository.findById(id);
	    
		return ResponseEntity.ok(member);
	}

	@Override
	 public List<Clubmembers> findByMembersWithTodayDobOrAnniversary() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayMonthDay = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd"));

        return memberRepository.findAll().stream().filter(member -> {
            return matchDate(member.getMemberdob(), todayMonthDay) ||
                   matchDate(member.getSpousedob(), todayMonthDay) ||
                   matchDate(member.getMemberaniversary(), todayMonthDay);
        }).collect(Collectors.toList());
    }

    private boolean matchDate(String dateStr, String todayMonthDay) {
        try {
            if (dateStr != null && !dateStr.isEmpty()) {
                LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String memberMonthDay = date.format(DateTimeFormatter.ofPattern("MM-dd"));
                return memberMonthDay.equals(todayMonthDay);
            }
        } catch (Exception e) {
            // handle parsing exception
        }
        return false;
    }


}
