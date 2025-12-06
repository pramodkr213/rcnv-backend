package com.main.jobilitybackend.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.dto.requestDTO.studentRequests.EducationRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.dto.responses.studentResponses.StudentDashboardResponse;
import com.main.jobilitybackend.dto.responses.studentResponses.StudentDataResponse;
import com.main.jobilitybackend.dto.responses.studentResponses.StudentProfileUpdate;
import com.main.jobilitybackend.jwtSecurity.JwtProvider;
import com.main.jobilitybackend.services.serviceInterface.EducationService;
import com.main.jobilitybackend.services.serviceInterface.JobApplicationService;
import com.main.jobilitybackend.services.serviceInterface.JobBookmarkService;
import com.main.jobilitybackend.services.serviceInterface.StudentService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/student")
@CrossOrigin
@Slf4j
public class StudentController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private JobApplicationService jobApplicationService;

	@Autowired
	private JobBookmarkService jobBookmarkService;

	@Autowired
	private EducationService educationService;

	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

	@GetMapping("/dashboard")
	public ResponseEntity<StudentDataResponse<StudentDashboardResponse>> getEmployerDashboard(
			HttpServletRequest request) throws Exception {
		log.info("Getting employer dashboard");
		try {
			Cookie[] cookies = request.getCookies();
			String token = null;
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("accessToken".equals(cookie.getName())) {
						token = cookie.getValue();
					}
				}
			}
			StudentDataResponse<StudentDashboardResponse> response = studentService.getStudentDashboard(token);
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@GetMapping("/job/{jobId}/applyJob")
	public ResponseEntity<DataResponse> applyJob(@PathVariable("jobId") String jobId,
			@CookieValue(value = "accessToken", required = false) String cookieToken) throws Exception {
		try {
			String token = null;
			if (cookieToken != null) {
				token = cookieToken;
			}
			DataResponse response = this.jobApplicationService.addJobApplication(token, jobId);

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@GetMapping("change_email_request")
	public ResponseEntity<DataResponse> changeEmailRequest(
			@CookieValue(value = "accessToken", required = false) String cookieToken,
			@RequestParam("newEmail") String newemail) throws Exception {
		try {
			String token = null;
			if (cookieToken != null) {
				token = cookieToken;
			}
			String email = JwtProvider.getEmailFromJwt(token);
			DataResponse response = this.studentService.changeEmailRequest(email, newemail);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@PutMapping("/profile/update")
	public ResponseEntity<DataResponse> updateProfile(
			@CookieValue(value = "accessToken", required = false) String cookieToken,
			@RequestBody StudentProfileUpdate studentProfileUpdate) throws Exception {
		try {
			String token = null;
			if (cookieToken != null) {
				token = cookieToken;
			}
			String email = JwtProvider.getEmailFromJwt(token);
			DataResponse response = this.studentService.updateStudentProfile(email, studentProfileUpdate);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@GetMapping("/appliedJobs")
	public ResponseEntity<List<DataResponse>> getAppliedJobs(
			@CookieValue(value = "accessToken", required = false) String cookieToken) throws Exception {
		try {
			String token = null;
			if (cookieToken != null) {
				token = cookieToken;
			}
			String email = JwtProvider.getEmailFromJwt(token);
			if (email == null) {
				throw new Exception("Unauthorized access");
			}

			DataResponse response = this.jobApplicationService.getStudentAppliedJobs(email);
			DataResponse response1 = this.jobApplicationService.getStudentAppliedInternships(email);
			
			logger.debug("Applied jobs and internships: {}", List.of(response, response1));
			return ResponseEntity.status(HttpStatus.OK).body(List.of(response,response1));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
            }
	


	@PostMapping("/bookmarkjob/{jobId}")
	public ResponseEntity<DataResponse> bookMarkJob(@PathVariable("jobId") String jobId,
			@CookieValue(value = "accessToken", required = false) String cookieToken) throws Exception {

		String token = null;
		if (cookieToken != null) {
			token = cookieToken;
		}
		String email = JwtProvider.getEmailFromJwt(token);
		if (email == null) {
			throw new Exception("Unauthorized access");
		}
		DataResponse response = this.jobBookmarkService.addJobBookmark(email, jobId);
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	@GetMapping("/bookmarkedJobs")
	public ResponseEntity<DataResponse> getBookmarkedJobs(
			@CookieValue(value = "accessToken", required = false) String cookieToken) throws Exception {
		try {
			String token = null;
			if (cookieToken != null) {
				token = cookieToken;
			}
			String email = JwtProvider.getEmailFromJwt(token);
			if (email == null) {
				throw new Exception("Unauthorized access");
			}

			DataResponse response = this.jobBookmarkService.getStudentBookMarkedJobs(email);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@GetMapping("/internships/{id}/apply")
	public ResponseEntity<DataResponse> applyInternship(@PathVariable("id") String internshipId,
			@CookieValue(value = "accessToken", required = false) String cookieToken) throws Exception {
		try {
			String token = null;
			if (cookieToken != null) {
				token = cookieToken;
			}
			DataResponse response = this.jobApplicationService.addInternshipApplication(token, internshipId);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@PostMapping("/bookmarkInternship/{internshipId}")
	public ResponseEntity<DataResponse> bookMarkInternship(@PathVariable("internshipId") String internshipId,
			@CookieValue(value = "accessToken", required = false) String cookieToken) throws Exception {
		try {
			String token = null;
			if (cookieToken != null) {
				token = cookieToken;
			}
			String email = JwtProvider.getEmailFromJwt(token);
			if (email == null) {
				throw new Exception("Unauthorized access");
			}
			DataResponse response = this.jobBookmarkService.addInternshipBookmark(email, internshipId);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@GetMapping("/bookmarkedInternships")
	public ResponseEntity<DataResponse> getBookmarkedInternships(
			@CookieValue(value = "accessToken", required = false) String cookieToken) throws Exception {
		try {
			String token = null;
			if (cookieToken != null) {
				token = cookieToken;
			}
			String email = JwtProvider.getEmailFromJwt(token);
			if (email == null) {
				throw new Exception("Unauthorized access");
			}

			DataResponse response = this.jobBookmarkService.getStudentBookMarkedInternships(email);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@GetMapping("/appliedInternships")
	public ResponseEntity<DataResponse> getAppliedInternships(
			@CookieValue(value = "accessToken", required = false) String cookieToken) throws Exception {
		try {
			String token = null;
			if (cookieToken != null) {
				token = cookieToken;
			}
			String email = JwtProvider.getEmailFromJwt(token);
			if (email == null) {
				throw new Exception("Unauthorized access");
			}

			DataResponse response = this.jobApplicationService.getStudentAppliedInternships(email);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	// Education for the Student
	@PostMapping("/education/add")
	public ResponseEntity<DataResponse> addEducation(@RequestBody EducationRequest request,
			@CookieValue(value = "accessToken", required = false) String cookieToken) throws Exception {
		try {
			String token = null;
			if (cookieToken != null) {
				token = cookieToken;
			}
			String email = JwtProvider.getEmailFromJwt(token);
			DataResponse response = this.educationService.addEducation(request, email);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@PutMapping("/education/update/{educationId}")
	public ResponseEntity<DataResponse> updateEducation(@RequestBody EducationRequest request,
			@PathVariable("educationId") Long educationId,
			@CookieValue(value = "accessToken", required = false) String cookieToken) throws Exception {
		try {
			String token = null;
			if (cookieToken != null) {
				token = cookieToken;
			}
			String email = JwtProvider.getEmailFromJwt(token);
			DataResponse response = this.educationService.updateEducation(request, educationId, email);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@DeleteMapping("/education/delete/{educationId}")
	public ResponseEntity<DataResponse> deleteEducation(@PathVariable("educationId") Long educationId,
			@CookieValue(value = "accessToken", required = false) String cookieToken) throws Exception {
		try {
			String token = null;
			if (cookieToken != null) {
				token = cookieToken;
			}
			String email = JwtProvider.getEmailFromJwt(token);
			DataResponse response = this.educationService.deleteEducation(educationId, email);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

	@PostMapping("/uploadProfilePicture")
	public ResponseEntity<SuccessResponse> uploadProfilePicture(
			@CookieValue(value = "accessToken", required = false) String cookieToken,
			@RequestParam("file") MultipartFile file) throws Exception {
		try {
			String token = null;
			if (cookieToken != null) {
				token = cookieToken;
			}
			String email = JwtProvider.getEmailFromJwt(token);
			SuccessResponse response = this.studentService.uploadImage(email, file);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@PostMapping("/uploadResume")
	public ResponseEntity<SuccessResponse> uploadResume(
			@CookieValue(value = "accessToken", required = false) String cookieToken,
			@RequestParam("file") MultipartFile file) throws Exception {
		try {
			String token = null;
			if (cookieToken != null) {
				token = cookieToken;
			}
			String email = JwtProvider.getEmailFromJwt(token);
			SuccessResponse response = this.studentService.uploadResume(email, file);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

}
