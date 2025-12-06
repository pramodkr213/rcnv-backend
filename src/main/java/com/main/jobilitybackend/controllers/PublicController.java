package com.main.jobilitybackend.controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.jobilitybackend.dto.requestDTO.UpdateEventRequest;
import com.main.jobilitybackend.dto.requestDTO.rcnv.GalleryRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.entities.Clubmembers;
import com.main.jobilitybackend.entities.Event;
import com.main.jobilitybackend.entities.Gallery;
import com.main.jobilitybackend.entities.Imagecategory;
import com.main.jobilitybackend.entities.Media;
import com.main.jobilitybackend.jwtSecurity.JwtProvider;
import com.main.jobilitybackend.service.EventService;
import com.main.jobilitybackend.service.ImageCatService;
import com.main.jobilitybackend.service.MembersService;
import com.main.jobilitybackend.services.serviceInterface.CategoryService;
import com.main.jobilitybackend.services.serviceInterface.ClubProjectService;
import com.main.jobilitybackend.services.serviceInterface.DirectorService;
import com.main.jobilitybackend.services.serviceInterface.DirectoryService;
import com.main.jobilitybackend.services.serviceInterface.GalleryService;
import com.main.jobilitybackend.services.serviceInterface.HeroSectionService;
import com.main.jobilitybackend.services.serviceInterface.IntershipService;
import com.main.jobilitybackend.services.serviceInterface.JobPostService;
import com.main.jobilitybackend.services.serviceInterface.OurSponsersService;
import com.main.jobilitybackend.services.serviceInterface.ProjectService;
import com.main.jobilitybackend.services.serviceInterface.SubCategoryService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*")
@Slf4j
public class PublicController {
	@Autowired
	private EventService eventService;
	@Autowired
	private JobPostService jobPostService;

	@Autowired
	private IntershipService intershipService;

	@Autowired
	private GalleryService galleryService;

	@Autowired
	private DirectorService directorService;

	@Autowired
	private DirectoryService directoryService;

	@Autowired
	private ClubProjectService clubProjectService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SubCategoryService subCategoryService;

	@Autowired
	private OurSponsersService ourSponsersService;

	@Autowired
	private HeroSectionService heroSectionService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ImageCatService imageCatService;
	@Autowired
	private MembersService membersService;

	@GetMapping("/jobs")
	public ResponseEntity<DataResponse> getAllJobs(@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) String title, @RequestParam(required = false) String location,
			@RequestParam(required = false) String jobType, @RequestParam(required = false) String mode,
			@RequestParam(required = false) Integer minExperience,
			@RequestParam(required = false) Integer maxExperience, @RequestParam(required = false) Boolean isFresher,
			@RequestParam(required = false) Long minSalary, @RequestParam(required = false) Long maxSalary,
			@RequestParam(required = false) Integer days, @RequestParam(required = false) String sector,
			@CookieValue(value = "accessToken", required = false) String cookieToken) throws Exception {

		String token = null;
		String email = null;
		if (cookieToken != null) {
			token = cookieToken;
		}
		if (token == null) {
			email = null;
		} else {
			try {
				email = JwtProvider.getEmailFromJwt(token);
			} catch (Exception e) {
				email = null;
			}
		}
		DataResponse response = this.jobPostService.getAllJobPosts(page, title, location, jobType, mode, minExperience,
				maxExperience, isFresher, minSalary, maxSalary, email, sector);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/job/{id}")
	public ResponseEntity<DataResponse> getJobPostById(@PathVariable("id") String id,
			@CookieValue(value = "accessToken", required = false) String cookieToken) throws Exception {
		String token = null;
		String email = null;
		if (cookieToken != null) {
			token = cookieToken;
		}
		if (token == null) {
			email = null;
		} else {
			try {
				email = JwtProvider.getEmailFromJwt(token);
			} catch (Exception e) {
				email = null;
			}
		}
		DataResponse response = jobPostService.getJobPostNotDeletedById(id, email);
		return ResponseEntity.status(200).body(response);

	}

	@GetMapping("/internships")
	public ResponseEntity<DataResponse> getAllInternships(@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) String title, @RequestParam(required = false) String location,
			@RequestParam(required = false) String internshipType, @RequestParam(required = false) Long minStipend,
			@RequestParam(required = false) Long maxStipend, @RequestParam(required = false) Boolean isPaid,
			@RequestParam(required = false) String mode,
			@CookieValue(value = "accessToken", required = false) String cookieToken) throws Exception {

		String token = null;
		String email = null;
		if (cookieToken != null) {
			token = cookieToken;
		}
		if (token == null) {
			email = null;
		} else {
			try {
				email = JwtProvider.getEmailFromJwt(token);
			} catch (Exception e) {
				email = null;
			}
		}
		DataResponse response = this.intershipService.getAllInterships(page, title, location, internshipType,
				minStipend, maxStipend, isPaid, mode, email);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/internship/{id}")
	public ResponseEntity<DataResponse> getInternshipPostById(@PathVariable("id") String id,
			@CookieValue(value = "accessToken", required = false) String cookieToken) throws Exception {

		String token = null;
		String email = null;
		if (cookieToken != null) {
			token = cookieToken;
		}
		if (token == null) {
			email = null;
		} else {
			try {
				email = JwtProvider.getEmailFromJwt(token);
			} catch (Exception e) {
				email = null;
			}
		}
		try {
			DataResponse response = intershipService.getIntershipPostById(id, email);
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@GetMapping("/check")
	public ResponseEntity<DataResponse> getAllJobs(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		log.debug("User-Agent: {}", userAgent);

		if (userAgent != null) {
			if (userAgent.toLowerCase().contains("android")) {
				log.debug("Request is from Android App");
			} else if (userAgent.toLowerCase().contains("mozilla")) {
				log.debug("Request is from Web Browser");
			} else {
				log.debug("Unknown Client");
			}
		}

		return ResponseEntity.ok(null);
	};

	@GetMapping("/hero-section")
	public ResponseEntity<DataResponse> getAllHeroSection() {
		DataResponse response = this.heroSectionService.getHeroSection();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

//    @GetMapping("/gallery")
//    public ResponseEntity<DataResponse> getGallery(@RequestParam(defaultValue = "0") int page) {
//        DataResponse response = this.galleryService.getAllGallery(page);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }

	@GetMapping("/gallery")
	public ResponseEntity<DataResponse> getGallery(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {

		DataResponse response = this.galleryService.getAllGallery(page, size);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/sponsers")
	public ResponseEntity<DataResponse> getOurSponsers() {
		DataResponse response = this.ourSponsersService.getAllOurSponsers();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/projects")
	public ResponseEntity<DataResponse> getAllProject(@RequestParam(required = false) Long clubProjectId,
			@RequestParam(required = false) Long categoryId, @RequestParam(required = false) Long subcategoryId,
			@RequestParam(required = false) String city, @RequestParam(required = false) String year,
			@RequestParam(defaultValue = "0") int page) {
		DataResponse response = this.projectService.getAllProjects(clubProjectId, categoryId, subcategoryId, city, year,
				page);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("projects/{projectId}")
	public ResponseEntity<DataResponse> getProjectsById(@PathVariable("projectId") long projectId) {
		DataResponse response = this.projectService.getProjectById(projectId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("club-projects")
	public ResponseEntity<DataResponse> getClubProjects() {
		DataResponse response = this.clubProjectService.getAllClubProjects();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("category/club/{clubId}")
	public ResponseEntity<DataResponse> getCategoryByClub(@PathVariable("clubId") long clubId) {
		DataResponse response = this.categoryService.getAllCategoriesByClub(clubId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("subCategory/category/{categoryId}")
	public ResponseEntity<DataResponse> getSubcategoryByCategory(@PathVariable("categoryId") long categoryId) {
		DataResponse response = this.subCategoryService.getAllSubCategoriesByCategory(categoryId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/directors")
	public ResponseEntity<DataResponse> getAllDirectors() {
		DataResponse response = this.directorService.getAllDirectors();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

//    -----------------sk-------------------------------

	@GetMapping("/allmedia")
	public ResponseEntity<Map<String, Object>> getAllMedia(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "6") int size) {

		Map<String, Object> response = galleryService.getAllMedia(page, size);
		return ResponseEntity.ok(response);
	}

//-----------------------sk events-----------------------

	@GetMapping("/upcoming-events")
	public ResponseEntity<?> getUpcomingEvents(@RequestParam(required = false) Integer page,
			@RequestParam(defaultValue = "6") Integer size) {

		log.info("Fetching upcoming events - page: {}, size: {}", page, size);
		LocalDate today = LocalDate.now(ZoneId.of("UTC"));
		log.info("Today's date (UTC): {}", today);

		try {
			if (page != null) {
				Page<Event> eventsPage = eventService.getUpcomingEvents(page, size);
				log.info("Found {} events", eventsPage.getNumberOfElements());

				Map<String, Object> response = new HashMap<>();
				response.put("events", eventsPage.getContent());
				response.put("currentPage", eventsPage.getNumber());
				response.put("totalItems", eventsPage.getTotalElements());
				response.put("totalPages", eventsPage.getTotalPages());

				return ResponseEntity.ok(response);
			} else {
				List<Event> events = eventService.getAllUpcomingEvents();
				log.info("Found {} total events", events.size());
				return ResponseEntity.ok(events);
			}
		} catch (Exception e) {
			log.error("Error fetching events", e);
			return ResponseEntity.internalServerError()
					.body(Map.of("success", false, "message", "Failed to fetch events", "error", e.getMessage()));
		}
	}

	@GetMapping("/eventbyid")
	public ResponseEntity<?> GeteventById(@RequestParam Long id) {
		return eventService.findById(id);

	}

	@DeleteMapping("/delete-today")
	public ResponseEntity<?> deleteTodayEvents() {
		eventService.deleteTodayEvents();
		return ResponseEntity.ok("Today's events deleted successfully");
	}

//  sk gallery get by catid----------------------------------------
	@GetMapping("/getgallerybycatid")
	public ResponseEntity<?> getAllByCatId(@RequestParam Long catid, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "6") int size) {

		return galleryService.findByCatid(catid, page, size);
	}

	@GetMapping("/getgallerybycid")
	public ResponseEntity<?> getGalleryBycidwitoutPagination(@RequestParam Long cid) {
		return galleryService.findByCidWithoutpagination(cid);
	}

//  sk --------------image category------------------------------------------------------------------

	@PostMapping("/saveimgcat")
	public ResponseEntity<?> saveImageCategory(@RequestBody Imagecategory imgcat) {

		return imageCatService.save(imgcat);

	}

	@GetMapping("/getimgcategory")
	public ResponseEntity<?> getAllImgcategory(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return imageCatService.getAllImgcategory(page, size);
	}

	@DeleteMapping("/deleteimgcatbyid")
	public ResponseEntity<?> deleteImgcategory(@RequestParam Long id) {
		return imageCatService.deleteImgCat(id);

	}

	@PutMapping("/updateimgcatdata")
	public ResponseEntity<?> updateImgcatById(@RequestBody Imagecategory data) {

		return imageCatService.updateImgCatData(data);

	}

//  -------------------sk--------------------member public api-----------------------

	@GetMapping("/getallmembers")
	public ResponseEntity<Map<String, Object>> getAllMembers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		return ResponseEntity.ok(membersService.findAllMembers(page, size));
	}

	@GetMapping("/getmemberbyid")
	public ResponseEntity<?> getmember(@RequestParam Long id) {

		return membersService.findMemberById(id);

	}

	@GetMapping("/getMembersByDobAndAniversary")
	public List<Clubmembers> getMembersByDobAndAniversary() {

		return membersService.findByMembersWithTodayDobOrAnniversary();

	}

}
