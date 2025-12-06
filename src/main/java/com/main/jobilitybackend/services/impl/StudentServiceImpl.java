	package com.main.jobilitybackend.services.impl;
	
	import java.time.Instant;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	import java.util.UUID;
	import java.util.concurrent.CompletableFuture;
	import java.util.stream.Collectors;
	
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.data.domain.PageRequest;
	import org.springframework.data.domain.Pageable;
	import org.springframework.http.HttpStatus;
	import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
	import org.springframework.security.core.Authentication;
	import org.springframework.security.core.userdetails.UserDetails;
	import org.springframework.security.core.userdetails.UsernameNotFoundException;
	import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
	import org.springframework.stereotype.Service;
	import org.springframework.web.multipart.MultipartFile;
	
	import com.main.jobilitybackend.Exceptions.DuplicateEntityException;
	import com.main.jobilitybackend.Exceptions.EntityNotFoundException;
	import com.main.jobilitybackend.Exceptions.UnauthorizeException;
	import com.main.jobilitybackend.dto.requestDTO.StudentRegisterRequest;
	import com.main.jobilitybackend.dto.responseDTO.DataResponse;
	import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
	import com.main.jobilitybackend.dto.responses.studentResponses.ChangeStudentEmail;
	import com.main.jobilitybackend.dto.responses.studentResponses.StudentDashboardResponse;
	import com.main.jobilitybackend.dto.responses.studentResponses.StudentDataResponse;
	import com.main.jobilitybackend.dto.responses.studentResponses.StudentProfileUpdate;
	import com.main.jobilitybackend.dto.responses.studentResponses.StudentResponse;
	import com.main.jobilitybackend.entities.Student;
	import com.main.jobilitybackend.helper.ImageUploader;
	import com.main.jobilitybackend.jwtSecurity.CustomStudentDetail;
	import com.main.jobilitybackend.jwtSecurity.JwtProvider;
	import com.main.jobilitybackend.repositories.EducationRepo;
	import com.main.jobilitybackend.repositories.JobApplicationRepo;
	import com.main.jobilitybackend.repositories.JobBookmarksRepo;
	import com.main.jobilitybackend.repositories.StudentRepo;
	import com.main.jobilitybackend.services.serviceInterface.EmailService;
	import com.main.jobilitybackend.services.serviceInterface.StudentService;
	
	import lombok.extern.slf4j.Slf4j;
	
	@Slf4j
	@Service
	public class StudentServiceImpl implements StudentService {
	
	    @Autowired
	    private StudentRepo studentRepo;
	
	    @Autowired
	    private EmailService emailService;
	
	    @Autowired
	    private CustomStudentDetail customStudentDetail;
	
	    @Autowired
	    private ImageUploader imageUploader;
	    
	    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
	
	    @Override
	    public DataResponse addStudent(StudentRegisterRequest request) throws Exception {
	        StudentResponse existstudent = this.studentRepo.findByEmailAndDeleteAtNull(request.getEmail()).orElse(null);
	        if (existstudent != null) {
	            throw new DuplicateEntityException("Admin Already Exists");
	        }
	        String id = UUID.randomUUID().toString().replace("-", "");
	        Student student = new Student();
	        student.setId(id);
	        student.setEmail(request.getEmail());
	        student.setPhone(request.getPhone());
	        student.setFirstName(request.getFirstName());
	        student.setLastName(request.getLastName());
	        student.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
	        student = this.studentRepo.save(student);
	        DataResponse response = DataResponse.builder()
	                .success(true)
	                .status(HttpStatus.CREATED)
	                .statusCode(201)
	                .message("Student Created Successfully")
	                .timestamp(Instant.now())
	                .data(student)
	                .build();
	        return response;
	    }
	
	    @Override
	    public String studentLogin(String email, String password, long expirationTime) throws Exception {
	        log.info("Logging in student with email: {}", email);
	        StudentResponse student = this.studentRepo.findByEmailAndDeleteAtNull(email).orElse(null);
	        if (student == null) {
	            log.error("Invalid email or password");
	            throw new EntityNotFoundException("invalid email or password");
	        }
	        UserDetails userDetails = this.customStudentDetail.loadUserByUsername(email);
	        if (userDetails == null) {
	            log.error("Invalid email or password");
	            throw new UnauthorizeException("Invalid Email or Password");
	        }
	        boolean isPasswordValid = new BCryptPasswordEncoder().matches(password, userDetails.getPassword());
	        if (!isPasswordValid) {
	            log.error("Invalid email or password");
	            throw new UnauthorizeException("Invalid Email or Password");
	        }
	
	        Authentication authentication = authenticate(email, password);
	        String token = JwtProvider.generateJwtToken(authentication, expirationTime);
	        log.info("Token generated successfully");
	        return token;
	
	    }
	
	    @Override
	    public DataResponse getStudentById(String id) throws Exception {
	
	        StudentResponse student = this.studentRepo.findByIdAndDeleteAtNull(id).orElse(null);
	        if (student == null) {
	            throw new EntityNotFoundException("Student not found");
	        }
	        DataResponse response = DataResponse.builder()
	                .success(true)
	                .status(HttpStatus.OK)
	                .statusCode(200)
	                .message("Student Retrieved Successfully")
	                .timestamp(Instant.now())
	                .data(student)
	                .build();
	        return response;
	
	    }
	
	    @Override
	    public DataResponse getStudentByEmail(String email) throws Exception {
	
	        StudentResponse student = this.studentRepo.findByEmailAndDeleteAtNull(email).orElse(null);
	        DataResponse response = DataResponse.builder()
	                .success(true)
	                .status(HttpStatus.CREATED)
	                .statusCode(201)
	                .message("Student Created Successfully")
	                .timestamp(Instant.now())
	                .data(student)
	                .build();
	        return response;
	
	    }
	
	    private Authentication authenticate(String email, String password) {
	        UserDetails userDetails = this.customStudentDetail.loadUserByUsername(email);
	        if (userDetails == null) {
	            throw new UsernameNotFoundException("bad credentials");
	        }
	        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
	    }
	
	    @Override
	    public DataResponse updateStudentProfile(String email, StudentProfileUpdate studentProfileUpdate) throws Exception {
	
	        Student student = this.studentRepo.findByEmail(email).orElse(null);
	        if (student == null) {
	            throw new EntityNotFoundException("Student not found");
	        }
	        student.setFirstName(studentProfileUpdate.getFirstName());
	        student.setLastName(studentProfileUpdate.getLastName());
	        student.setPhone(studentProfileUpdate.getPhone());
	        student.setDob(studentProfileUpdate.getDob());
	        student.setGender(studentProfileUpdate.getGender());
	        student.setCity(studentProfileUpdate.getCity());
	        student.setState(studentProfileUpdate.getState());
	        student.setCountry(studentProfileUpdate.getCountry());
	        student.setLanuagesKnown(studentProfileUpdate.getLanuagesKnown());
	        student.setAboutMe(studentProfileUpdate.getAboutMe());
	        student.setCareerObjective(studentProfileUpdate.getCareerObjective());
	        student.setLinkedinProfile(studentProfileUpdate.getLinkedinProfile());
	        student.setGithubProfile(studentProfileUpdate.getGithubProfile());
	        student.setPortfolio(studentProfileUpdate.getPortfolio());
	
	        student = this.studentRepo.save(student);
	        DataResponse response = DataResponse.builder()
	                .success(true)
	                .status(HttpStatus.OK)
	                .statusCode(200)
	                .message("Student Profile Updated Successfully")
	                .timestamp(Instant.now())
	                .data(student)
	                .build();
	        return response;
	
	    }
	
	    @Override
	    public DataResponse changeStudentEmail(ChangeStudentEmail request) throws Exception {
	
	        Student student = this.studentRepo.findByEmail(request.getOldEmail()).orElse(null);
	        if (student == null) {
	            throw new EntityNotFoundException("Student not found");
	        }
	        StudentResponse existStudent = this.studentRepo.findByEmailAndDeleteAtNull(request.getNewEmail()).orElse(null);
	        if (existStudent != null) {
	            throw new DuplicateEntityException(request.getNewEmail() + " already exists");
	        }
	        student.setEmail(request.getNewEmail());
	        student.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
	        student = this.studentRepo.save(student);
	        DataResponse response = DataResponse.builder()
	                .success(true)
	                .status(HttpStatus.OK)
	                .statusCode(200)
	                .message("Student Email Changed Successfully")
	                .timestamp(Instant.now())
	                .data(student)
	                .build();
	        return response;
	
	    }
	
	    @Override
	    public DataResponse changeEmailRequest(String email, String newEmail) throws Exception {
	
	        StudentResponse student = this.studentRepo.findByEmailAndDeleteAtNull(email).orElse(null);
	        if (student == null) {
	            throw new EntityNotFoundException("Student not found");
	        }
	        StudentResponse existStudent = this.studentRepo.findByEmailAndDeleteAtNull(newEmail).orElse(null);
	        if (existStudent != null) {
	            throw new DuplicateEntityException(newEmail + " already exists");
	        }
	        CompletableFuture.runAsync(() -> emailService.sendChangeEmailRequest(email, newEmail));
	        DataResponse response = DataResponse.builder()
	                .success(true)
	                .status(HttpStatus.OK)
	                .statusCode(200)
	                .message("Change Email Request Sent Successfully")
	                .timestamp(Instant.now())
	                .data(student)
	                .build();
	        return response;
	
	    }
	
	    // @Override
	    // public DataResponse getAllStudents(int page, String keywords, String city, String date, String degree,
	    //         String college, String fieldOfStudy, String yearOfPassing) throws Exception {
	    //     int pageSize = 10; // Define the number of students per page
	    //     Pageable pageable = PageRequest.of(page, pageSize);
	    //     if (page < 0) {
	    //         throw new IllegalArgumentException("Page number cannot be negative");
	    //     }                       
	    //     List<Map<String,Object>> students = this.studentRepo.findAllByFilters(keywords, city, date, degree,
	    //             college, fieldOfStudy, yearOfPassing, pageable);
	
	    //     DataResponse response = DataResponse.builder()
	    //             .success(true)
	    //             .status(HttpStatus.OK)
	    //             .statusCode(200)
	    //             .message("Students Retrieved Successfully")
	    //             .timestamp(Instant.now())
	    //             .additionalData(students)
	    //             .build();
	    //     return response;
	    // }
	
	    @Override
	public DataResponse getAllStudents(int page, String keywords, String city, String date, String degree,
	        String college, String fieldOfStudy, String yearOfPassing) throws Exception {
	    int pageSize = 10;
	    Pageable pageable = PageRequest.of(page, pageSize);
	    
	    logger.info("[StudentService:getAllStudents] Fetching students | Page: {}, Filters => keywords: {}, city: {}, date: {}, degree: {}, college: {}, fieldOfStudy: {}, yearOfPassing: {}",
	            page, keywords, city, date, degree, college, fieldOfStudy, yearOfPassing);
	    
	    List<Student> students = this.studentRepo.findAllByFilters(
	            keywords, city, date, degree, college, fieldOfStudy, yearOfPassing, pageable);
	
	    // Convert to List<Map>
	    List<Map<String, Object>> studentMaps = students.stream()
	            .map(student -> {
	                Map<String, Object> map = new HashMap<>();
	                map.put("id", student.getId());
	                map.put("firstName", student.getFirstName());
	                map.put("lastName", student.getLastName());
	                map.put("email", student.getEmail());
	                map.put("phone", student.getPhone());       
	                map.put("dob", student.getDob());
	                map.put("role", student.getRole());
	
	                map.put("skills", student.getSkills());
	                map.put("resumeLink,", student.getResumeLink());
	                map.put("profilePicture", student.getProfilePicture());
	                map.put("city", student.getCity());
	                map.put("state", student.getState());
	                map.put("country", student.getCountry());
	                map.put("linkedinProfile", student.getLinkedinProfile());
	                map.put("githubProfile", student.getGithubProfile());
	                map.put("aboutMe", student.getAboutMe());
	                map.put("active", student.isActive());
	                map.put("createdAt", student.getCreatedAt());
	                map.put("updatedAt,", student.getUpdatedAt());
	                map.put("education,", student.getEducation());
	
	                return map;
	            })
	            .collect(Collectors.toList());
	    
	    logger.info("[StudentService:getAllStudents] {} students fetched successfully", studentMaps.size());
	
	    DataResponse response = DataResponse.builder()
	            .success(true)
	            .status(HttpStatus.OK)
	            .statusCode(200)
	            .message("Students Retrieved Successfully")
	            .timestamp(Instant.now())
	            .studentData(studentMaps)
	            .build();
	    return response;
	}
	
	    @Override
	    public SuccessResponse activeAndInactiveStudent(String id) throws Exception {
	    	 logger.info("[activeAndInactiveStudent] Request received to toggle student status. Student ID: {}", id);
	
	        Student student = this.studentRepo.findById(id).orElse(null);
	        
	        if (student == null) {
	        	logger.warn("[activeAndInactiveStudent] Student not found for ID: {}", id);
	            throw new EntityNotFoundException("Student not found");
	        }
	        if (student.isActive()) {
	            this.studentRepo.activeAndInactive(id, false);
	            SuccessResponse response = SuccessResponse.builder()
	                    .success(true)
	                    .status(HttpStatus.OK)
	                    .statusCode(200)
	                    .message("Student Inactive Successfully")
	                    .timestamp(Instant.now())
	                    .build();
	            return response;
	        } else {
	            this.studentRepo.activeAndInactive(id, true);
	            SuccessResponse response = SuccessResponse.builder()
	                    .success(true)
	                    .status(HttpStatus.OK)
	                    .statusCode(200)
	                    .message("Student Active Successfully")
	                    .timestamp(Instant.now())
	                    .build();
	            return response;
	        }
	    }
	
	    @Override
	    public SuccessResponse deleteStudent(String id) throws Exception {
	    	logger.info("[DeleteStudent] Request received Student ID: {}", id);
	
	        Student student = this.studentRepo.findById(id).orElse(null);
	        if (student == null) {
	        	logger.info("[DeleteStudent] Student not found Student ID: {}", id);
	            throw new EntityNotFoundException("Student not found");
	        }
	        this.studentRepo.softDeleteStudentById(id);
	        ;
	        SuccessResponse response = SuccessResponse.builder()
	                .success(true)
	                .status(HttpStatus.OK)
	                .statusCode(200)
	                .message("Student Deleted Successfully")
	                .timestamp(Instant.now())
	                .build();
	        return response;
	    }
	
	    @Override
	    public SuccessResponse uploadImage(String email, MultipartFile file) throws Exception {
	        Student student = this.studentRepo.findByEmail(email).orElse(null);
	        if (student == null) {
	            throw new EntityNotFoundException("Student not found");
	        }
	        if (file.isEmpty() || file.getSize() <= 0) {
	            throw new IllegalArgumentException("File is empty");
	        }
	        try {
	            String image = imageUploader.imageUploader(file);
	            student.setProfilePicture(image);
	        } catch (Exception e) {
	            throw new Exception("Image upload failed: " + e.getMessage());
	        }
	        student = this.studentRepo.save(student);
	        SuccessResponse response = SuccessResponse.builder()
	                .success(true)
	                .status(HttpStatus.OK)
	                .statusCode(200)
	                .message("Image Uploaded Successfully")
	                .timestamp(Instant.now())
	                .build();
	        return response;
	    }
	
	    @Override
	    public SuccessResponse uploadResume(String email, MultipartFile file) throws Exception {
	        Student student = this.studentRepo.findByEmail(email).orElse(null);
	        if (student == null) {
	            throw new EntityNotFoundException("Student not found");
	        }
	        if (file.isEmpty() || file.getSize() <= 0) {
	            throw new IllegalArgumentException("File is empty");
	        }
	        try {
	            String resume = imageUploader.resumeUploader(file);
	            student.setResumeLink(resume);
	        } catch (Exception e) {
	            throw new Exception("Resume upload failed: " + e.getMessage());
	        }
	        student = this.studentRepo.save(student);
	        SuccessResponse response = SuccessResponse.builder()
	                .success(true)
	                .status(HttpStatus.OK)
	                .statusCode(200)
	                .message("Resume Uploaded Successfully")
	                .timestamp(Instant.now())
	                .build();
	        return response;
	    }
	
	    @Override
	    public SuccessResponse updateCareerObjectives(String email, String carrerObjective) {
	        this.studentRepo.updateCareerObjectives(email, carrerObjective);
	        SuccessResponse response = SuccessResponse.builder()
	                .success(true)
	                .timestamp(Instant.now())
	                .message("career objective update successfully")
	                .status(HttpStatus.OK)
	                .statusCode(200)
	                .build();
	        return response;
	    }
	
	    @Override
	    public SuccessResponse updateAboutMe(String email, String aboutMe) {
	        this.studentRepo.updateAboutMe(email, aboutMe);
	        SuccessResponse response = SuccessResponse.builder()
	                .success(true)
	                .timestamp(Instant.now())
	                .message("About me update successfully")
	                .status(HttpStatus.OK)
	                .statusCode(200)
	                .build();
	        return response;
	    }
	    
	    
	    
	    @Autowired
	    private JobApplicationRepo applicationRepo;
	
	    @Autowired
	    private JobBookmarksRepo jobBookmarksRepo;
	
	    @Autowired
	    private EducationRepo educationRepo;
	    
	   
	    @Override
	    public StudentDataResponse<StudentDashboardResponse> getStudentDashboard(String jwt) throws Exception {
	        String email = JwtProvider.getEmailFromJwt(jwt);
	        Student student = studentRepo.findByEmailAndDeleteAtIsNull(email)
	                .orElseThrow(() -> new DuplicateEntityException("Student not found"));
	
	        String studentId = student.getId();
	
	        Long totalApplications = applicationRepo.countByApplicant_Id(studentId);
	        Long totalBookmarks = jobBookmarksRepo.countByStudentIdAndDeleteAtIsNull(studentId);
	        Long educationCount = educationRepo.countByStudentIdAndDeleteAtIsNull(studentId);
	
	        // Skill and experience counts are currently commented out
	        // Long experienceCount = experienceRepo.countByStudentId(studentId);
	        // Long skillCount = skillRepo.countByStudentId(studentId);
	
	        int profileCompletion = calculateProfileCompletion(student, educationCount);
	
	        StudentDashboardResponse dashboard = new StudentDashboardResponse(
	                String.valueOf(totalApplications),
	                String.valueOf(totalBookmarks),
	                profileCompletion,
	                educationCount
	                
	                
	        );
	
	        return StudentDataResponse.<StudentDashboardResponse>builder()
	                .success(true)
	                .statusCode(200)
	                .message("Student Dashboard Retrieved Successfully")
	                .timestamp(Instant.now())
	                .data(dashboard)
	                .build();
	    }
	
	    private int calculateProfileCompletion(Student student, Long educationCount) {
	        int completion = 0;
	        if (student.getFirstName() != null && student.getLastName() != null) completion += 20;
	        if (educationCount > 0) completion += 20;
	        // if (experienceCount > 0) completion += 20;
	        // if (skillCount > 0) completion += 20;
	        if (student.getResumeLink() != null) completion += 20;
	        return completion;
	    }
	
	}