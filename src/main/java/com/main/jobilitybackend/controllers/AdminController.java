package com.main.jobilitybackend.controllers;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.main.jobilitybackend.dto.requestDTO.rcnv.DirectorRequest;
import com.main.jobilitybackend.dto.requestDTO.rcnv.GalleryRequest;
import com.main.jobilitybackend.dto.requestDTO.rcnv.ProjectRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.entities.ClubProject;
import com.main.jobilitybackend.entities.Clubmembers;
import com.main.jobilitybackend.entities.Event;
import com.main.jobilitybackend.entities.Imagecategory;
import com.main.jobilitybackend.entities.Media;
import com.main.jobilitybackend.service.EventService;
import com.main.jobilitybackend.service.ImageCatService;
import com.main.jobilitybackend.service.MembersService;
import com.main.jobilitybackend.services.serviceInterface.AdminService;
import com.main.jobilitybackend.services.serviceInterface.CategoryService;
import com.main.jobilitybackend.services.serviceInterface.ClubProjectService;
import com.main.jobilitybackend.services.serviceInterface.DirectorService;
import com.main.jobilitybackend.services.serviceInterface.DirectoryService;
import com.main.jobilitybackend.services.serviceInterface.EmployerService;
import com.main.jobilitybackend.services.serviceInterface.GalleryService;
import com.main.jobilitybackend.services.serviceInterface.HeroSectionService;
import com.main.jobilitybackend.services.serviceInterface.IntershipService;
import com.main.jobilitybackend.services.serviceInterface.JobApplicationService;
import com.main.jobilitybackend.services.serviceInterface.JobPostService;
import com.main.jobilitybackend.services.serviceInterface.OurSponsersService;
import com.main.jobilitybackend.services.serviceInterface.ProjectService;
import com.main.jobilitybackend.services.serviceInterface.StudentService;
import com.main.jobilitybackend.services.serviceInterface.SubCategoryService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
@Slf4j
public class AdminController {

    @Autowired
    private EmployerService employerService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private IntershipService intershipService;

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private OurSponsersService ourSponsersService;

    @Autowired
    private HeroSectionService heroSectionService;

    @Autowired
    private ClubProjectService clubProjectService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DirectorService directorService;

    @Autowired
    private DirectoryService directoryService;
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private ImageCatService imageCatService;
    @Autowired
    private MembersService membersService;
    
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/dashboard")
    public ResponseEntity<DataResponse> getAdminDashboard() throws Exception {
        try {
            DataResponse response = adminService.getAdminDashboard();
            logger.info("Admin dashboard data fetched successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while fetching admin dashboard data", e);
            throw e; 
        }
    }

    // Employer services handle by admin

    @GetMapping("/employer/request")
    public ResponseEntity<DataResponse> getPendingEmployerRequest(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "companyName", required = false) String companyName,
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "industryType", required = false) String industryType) throws Exception {
    	
    	 logger.info(" GET /admin/employer/request called with page={}, query={}, companyName={}, city={}, industryType={}",
                 page, query, companyName, city, industryType);

        DataResponse response = employerService.getPendingEmployerRequest(page, query, companyName, city, industryType);
        
        logger.info("[API] Employer request list fetched successfully with {} records", 
                ((List<?>) response.getData()).size());;
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employers")
    public ResponseEntity<DataResponse> getEmployers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "companyName", required = false) String companyName,
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "industryType", required = false) String industryType,
            @RequestParam(name = "date", required = false) String date) throws Exception {
    	
    	logger.info("[Controller] GET /employers | page={}, query={}, companyName={}, city={}, industryType={}, date={}",
                page, query, companyName, city, industryType, date);
    	
        DataResponse response = employerService.getEmployers(page, query, companyName, city, industryType, date);
      
        logger.info("[Controller] Employers DataResponse {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employer/{id}")
    public ResponseEntity<DataResponse> getEmployerByIdForAdmin(@PathVariable("id") Long id) throws Exception {
    	 logger.info("[AdminController] Request received to fetch employer details for ID: {}", id);
        DataResponse response = employerService.getEmployerByIdForAdmin(id);
        logger.info("Employer data fetched for ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("verify_employer/{id}")
    public ResponseEntity<SuccessResponse> verifyEmployer(@PathVariable("id") Long id) throws Exception {
    	 logger.info("[AdminController] Request received to verifyEmployer ID: {}", id);
        SuccessResponse response = this.employerService.varifyEmployer(id);
        logger.info("verifyEmployer Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("active_inactive_employer/{id}")
    public ResponseEntity<SuccessResponse> activeAndInactiveEmployer(@PathVariable("id") Long id) throws Exception {
    	logger.info("[AdminController] Request received to activeAndInactiveEmployer ID: {}", id);
    	SuccessResponse response = this.employerService.activeAndIncativeEmployer(id);
    	logger.info("activeAndInactiveEmployer Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete_employer/{id}")
    public ResponseEntity<SuccessResponse> deleteEmployer(@PathVariable("id") Long id) throws Exception {
    	logger.info("[AdminController: DeleteEmployer ] Request received to deleteEmployer ID: {}", id);
        SuccessResponse response = this.employerService.deleteEmployer(id);
        logger.info("DeleteEmployer Response: {}", response);
        return ResponseEntity.ok(response);
    }

    // Jobs service Handle by admin

    @GetMapping("/jobs")
    public ResponseEntity<DataResponse> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String date) throws Exception {
    	  logger.info("[AdminController:getAllJobs] Request received | page: {}, title: {}, location: {}, company: {}, date: {}",
    	            page, title, location, companyName, date);
        DataResponse response = this.jobPostService.getAllJobsForAdmin(page, title, location, companyName, date);
        logger.info("getAllJobs Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("active_inactive_job/{id}")
    public ResponseEntity<SuccessResponse> activeAndInactiveJobPost(@PathVariable("id") String id) throws Exception {
       
    	logger.info("[AdminController: activeAndInactiveJobPost ] Request received to "
    			+ "ActiveAndInactiveJobPost ID: {}", id);
    	SuccessResponse response = this.jobPostService.activeAndInactiveJobPost(id);
    	logger.info("ActiveAndInactiveJobPost Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete_job/{id}")
    public ResponseEntity<SuccessResponse> deleteJobPost(@PathVariable("id") String id) throws Exception {
    	
    	logger.info("[AdminController: deleteJobPost ] Request received to "
    			+ "DeleteJobPost ID: {}", id);
        SuccessResponse response = this.jobPostService.deleteJobPost(id);
        logger.info("DeleteJobPost Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent_jobs")
    public ResponseEntity<DataResponse> getRecentTop5Jobs() throws Exception {
    	logger.info("[AdminController: getRecentTop5Jobs ] ");
        DataResponse response = this.jobPostService.getRecentTop5Jobs();
        logger.info("Get RecentTop5Jobs Response: {}", response);
        return ResponseEntity.ok(response);
    }

    // Student services handle by admin

    @GetMapping("students")
    public ResponseEntity<DataResponse> getAllStudents(@RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "keywords", required = false) String keywords,
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "degree", required = false) String degree,
            @RequestParam(name = "college", required = false) String college,
            @RequestParam(name = "fieldOfStudy", required = false) String fieldOfStudy,
            @RequestParam(name = "yearOfPassing", required = false) String yearOfPassing) throws Exception {
    	
    	 logger.info("[AdminController:getAllStudents] Page: {}, Filters => keywords: {}, city: {}, date: {}, degree: {}, college: {}, fieldOfStudy: {}, yearOfPassing: {}",
    	            page, keywords, city, date, degree, college, fieldOfStudy, yearOfPassing);
    	
        DataResponse response = this.studentService.getAllStudents(page, keywords, city, date, degree, college,
                fieldOfStudy, yearOfPassing);
        logger.info("[AdminController:getAllStudents] Response sent with {} students", 
                response.getData() instanceof List ? ((List<?>) response.getData()).size() : "unknown count");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<DataResponse> getStudentById(@PathVariable("id") String id) throws Exception {
    	logger.info("[AdminController: getStudentById ] Request received to "
    			+ "GetStudentById ID: {}", id);
        DataResponse response = this.studentService.getStudentById(id);
        logger.info("Get StudentById Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("active_inactive_student/{id}")
    public ResponseEntity<SuccessResponse> activeAndInactiveStudent(@PathVariable("id") String id) throws Exception {
    	logger.info("[AdminController: ActiveAndInactiveStudent ] Request received to "
    			+ "ActiveAndInactiveStudent ID: {}", id);
        SuccessResponse response = this.studentService.activeAndInactiveStudent(id);
        logger.info("Get ActiveAndInactiveStudent Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete_student/{id}")
    public ResponseEntity<SuccessResponse> deleteStudent(@PathVariable("id") String id) throws Exception {
    	logger.info("[AdminController: DeleteStudent ] Request received to "
    			+ "DeleteStudent ID: {}", id);
        SuccessResponse response = this.studentService.deleteStudent(id);
        logger.info("Get DeleteStudent Response: {}", response);
        return ResponseEntity.ok(response);
    }

    // Internship services handle by admin

    @GetMapping("/internships")
    public ResponseEntity<DataResponse> getAllInterships(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String date) throws Exception {
    	
    	 logger.info("[GET /internships] Fetch internships | page: {}, title: {}, location: {}, companyName: {}, date: {}",
    	            page, title, location, companyName, date);
    	 
        DataResponse response = this.intershipService.getAllInterships(page, title, location, companyName, date);
        logger.info("GetAllInterships Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("active_inactive_internship/{id}")
    public ResponseEntity<SuccessResponse> activeAndInactiveIntershipPost(@PathVariable("id") String id)
            throws Exception {
        SuccessResponse response = this.intershipService.activeAndInactiveIntershipPost(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete_internship/{id}")
    public ResponseEntity<SuccessResponse> deleteIntershipPost(@PathVariable("id") String id) throws Exception {
        SuccessResponse response = this.intershipService.deleteIntershipPost(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent_internships")
    public ResponseEntity<DataResponse> getRecentTop5Interships() throws Exception {
        DataResponse response = this.intershipService.getRecentTop5Interships();
        return ResponseEntity.ok(response);
    }

    // Applications Service Handle by Admin

    @GetMapping("/applications")
    public ResponseEntity<DataResponse> getAllApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "companyName", required = false) String companyName,
            @RequestParam(name = "location", required = false) String location) throws Exception {
        DataResponse response = this.jobApplicationService.getAllApplications(page, query, date, status, companyName,
                location);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/internship/applications")
    public ResponseEntity<DataResponse> getAllInternshipApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "companyName", required = false) String companyName,
            @RequestParam(name = "location", required = false) String location) throws Exception {
        DataResponse response = this.jobApplicationService.getAllInternshipApplications(page, query, date, status,
                companyName, location);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/job/applications")
    public ResponseEntity<DataResponse> getAllJobApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "companyName", required = false) String companyName,
            @RequestParam(name = "location", required = false) String location) throws Exception {
        DataResponse response = this.jobApplicationService.getAllJobApplications(page, query, date, status, companyName,
                location);
        return ResponseEntity.ok(response);
    }

    // Hero section CRUD for admin

    @PostMapping("/hero-section")
    public ResponseEntity<DataResponse> addHeroSection(
            @RequestPart(name = "image", required = true) MultipartFile image,
            @RequestPart(name = "title", required = false) String title) throws Exception {
        DataResponse response = this.heroSectionService.addHeroSection(image, title);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/hero-section/{id}")
    public ResponseEntity<DataResponse> getHeroSectionById(@PathVariable("id") long id) throws Exception {
        DataResponse response = this.heroSectionService.getHeroSectionById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/hero-section/{id}")
    public ResponseEntity<SuccessResponse> deleteHeroSection(@PathVariable("id") long id) throws Exception {
        SuccessResponse response = this.heroSectionService.softDeleteHeroSection(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/hero-section/{id}")
    public ResponseEntity<SuccessResponse> updateHeroSection(@PathVariable("id") long id,
            @RequestPart(name = "image", required = false) MultipartFile image, @RequestPart("title") String title)
            throws Exception {
        SuccessResponse response = this.heroSectionService.updateHeroSection(id, image, title);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Gallery CRUD for admin

    @PostMapping("/gallery")
    public ResponseEntity<DataResponse> addGallary(
    		 @RequestParam("catId") Long catid,
            @RequestPart(name = "images", required = true) List<MultipartFile> images) throws Exception {
        DataResponse response = this.galleryService.createGallery(catid,images);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/gallery/{id}")
    public ResponseEntity<DataResponse> getGalleryById(@PathVariable("id") long id) throws Exception {
        DataResponse response = this.galleryService.getGalleryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//    @PutMapping("/gallery/{id}")
//    public ResponseEntity<SuccessResponse> updateGallery(@PathVariable("id") long id,
//            @RequestPart(name = "image", required = false) MultipartFile image,
//            @RequestPart("gallery") GalleryRequest request) throws Exception {
//        SuccessResponse response = this.galleryService.updateGallery(id, image, request);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
    
    @PutMapping("/gallery/{id}")
    public ResponseEntity<SuccessResponse> updateGallery(
            @PathVariable Long id,
            @RequestPart("gallery") String galleryJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        ObjectMapper objectMapper = new ObjectMapper();
        GalleryRequest request;

        try {
            request = objectMapper.readValue(galleryJson, GalleryRequest.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(galleryService.updateGallery(id, image, request));
    }


    @DeleteMapping("/gallery/{id}")
    public ResponseEntity<SuccessResponse> deleteGallery(@PathVariable("id") long id) throws Exception {
        SuccessResponse response = this.galleryService.softDeleteGallery(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Our Sponsers CRUD for admin

    @PostMapping("/sponsers")
    public ResponseEntity<DataResponse> addSponsers(@RequestPart("images") List<MultipartFile> images) {
        DataResponse response = this.ourSponsersService.createOurSponser(images);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("sponsers/{id}")
    public ResponseEntity<SuccessResponse> addSponsers(@PathVariable("id") long id) {
        SuccessResponse response = this.ourSponsersService.softDeleteOurSponser(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Club Project CRUD For Admin

    @PostMapping("/club-project")
    public ResponseEntity<DataResponse> addClubProject(@RequestPart("name") String name) {
        DataResponse response = this.clubProjectService.createClubProject(name);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/club-project/{id}")
    public ResponseEntity<DataResponse> getClubProjectById(@PathVariable("id") long id) {
        DataResponse response = this.clubProjectService.getClubProjectById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/club-project/{id}")
    public ResponseEntity<SuccessResponse> updateClubProject(@PathVariable("id") long id,
            @RequestPart("name") String name) {
        ClubProject clubProject = new ClubProject();
        clubProject.setName(name);
        SuccessResponse response = this.clubProjectService.updateClubProject(id, clubProject);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/club-project/{id}")
    public ResponseEntity<SuccessResponse> deleteClubProject(@PathVariable("id") long id) {
        SuccessResponse response = this.clubProjectService.deleteClubProject(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Category CRUD for admin

    @PostMapping("/category/{clubProjectId}")
    public ResponseEntity<DataResponse> addCategory(@RequestPart("name") String name,
            @PathVariable("clubProjectId") long clubProjectId) {
        DataResponse response = this.categoryService.createCategory(clubProjectId, name);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/categories")
    public ResponseEntity<DataResponse> getAllCategories() {
        DataResponse response = this.categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/categories/club/{clubId}")
    public ResponseEntity<DataResponse> getAllCategoriesByClub(@PathVariable("clubId") long clubId) {
        DataResponse response = this.categoryService.getAllCategoriesByClub(clubId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<DataResponse> getCategoryById(@PathVariable("id") Long id) {
        DataResponse response = this.categoryService.getCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<SuccessResponse> updateCategory(@PathVariable("id") Long id,
            @RequestPart("name") String name) {
        SuccessResponse response = this.categoryService.updateCategory(id, name);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<SuccessResponse> softDeleteCategory(@PathVariable("id") Long id) {
        SuccessResponse response = this.categoryService.softDeleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // SubCategory CRUD for admin

    @PostMapping("/subcategory/{categoryId}")
    public ResponseEntity<DataResponse> addSubCategory(@RequestPart("name") String name,
            @PathVariable("categoryId") Long categoryId) {
        DataResponse response = this.subCategoryService.createSubCategory(categoryId, name);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/subcategories")
    public ResponseEntity<DataResponse> getAllSubCategories() {
        DataResponse response = this.subCategoryService.getAllSubCategories();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/subcategories/category/{categoryId}")
    public ResponseEntity<DataResponse> getAllSubCategoriesByCategory(@PathVariable("categoryId") Long categoryId) {
        DataResponse response = this.subCategoryService.getAllSubCategoriesByCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/subcategory/{id}")
    public ResponseEntity<DataResponse> getSubCategoryById(@PathVariable("id") Long id) {
        DataResponse response = this.subCategoryService.getSubCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/subcategory/{id}")
    public ResponseEntity<SuccessResponse> updateSubCategory(@PathVariable("id") Long id,
            @RequestPart("name") String name) {
        SuccessResponse response = this.subCategoryService.updateSubCategory(id, name);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/subcategory/{id}")
    public ResponseEntity<SuccessResponse> deleteSubCategory(@PathVariable("id") Long id) {
        SuccessResponse response = this.subCategoryService.deleteSubCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // project CRUD for admin

    @PostMapping("/project")
    public ResponseEntity<DataResponse> addProject(@RequestPart("project") ProjectRequest projectRequest,
            @RequestPart(name = "images", required = false) List<MultipartFile> images) throws Exception
    {
        DataResponse response = this.projectService.createProject(projectRequest, images);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<DataResponse> getProjectById(@PathVariable("id") Long id) throws Exception {
        DataResponse response = this.projectService.getProjectById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//    @PutMapping("/project/{id}")
//    public ResponseEntity<SuccessResponse> updateProject(@PathVariable("id") Long id,@RequestPart("project") ProjectRequest projectRequest,
//            @RequestPart(name = "images", required = false) List<MultipartFile> images) throws Exception {
//        SuccessResponse response = this.projectService.updateProject(id, projectRequest , images);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
    @PutMapping("/project/{id}")
    public ResponseEntity<SuccessResponse> updateProject(
        @PathVariable("id") Long id,
        @RequestParam("project") String projectJson,  // Accept as String
        @RequestPart(name = "images", required = false) List<MultipartFile> images
    ) throws Exception {
        ProjectRequest projectRequest = new ObjectMapper().readValue(projectJson, ProjectRequest.class);
        SuccessResponse response = projectService.updateProject(id, projectRequest, images);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/project/{id}")
    public ResponseEntity<SuccessResponse> deleteProject(@PathVariable("id") Long id) throws Exception {
        SuccessResponse response = this.projectService.softDeleteProject(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/project/{id}/images")
    public ResponseEntity<DataResponse> addProjectImages(@PathVariable("id") Long id,
            @RequestPart("images") List<MultipartFile> images) throws Exception {
        DataResponse response = this.projectService.addProjectImages(id, images);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Director CRUD for admin

    @PostMapping("/director")
    public ResponseEntity<SuccessResponse> addDirector(@RequestPart("director") DirectorRequest request,
            @RequestPart(name = "image", required = false) org.springframework.web.multipart.MultipartFile image) {
        SuccessResponse response = this.directorService.addDirector(request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/directors")
    public ResponseEntity<DataResponse> getAllDirectors() {
        DataResponse response = this.directorService.getAllDirectors();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/director/{id}")
    public ResponseEntity<DataResponse> getDirectorById(@PathVariable("id") Long id) {
        DataResponse response = this.directorService.getDirectorById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/director/{id}")
    public ResponseEntity<SuccessResponse> updateDirector(@PathVariable("id") Long id,
            @RequestPart("director") DirectorRequest request,
            @RequestPart(name = "image", required = false) org.springframework.web.multipart.MultipartFile image) {
        SuccessResponse response = this.directorService.updateDirector(id, request, image);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/director/{id}")
    public ResponseEntity<SuccessResponse> softDeleteDirector(@PathVariable("id") Long id) {
        SuccessResponse response = this.directorService.softDeleteDirector(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/director/{id}/hard")
    public ResponseEntity<SuccessResponse> deleteDirector(@PathVariable("id") Long id) {
        SuccessResponse response = this.directorService.deleteDirector(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Directory CRUD for admin

    @PostMapping("/directory")
    public ResponseEntity<SuccessResponse> addDirectory(@RequestPart("directory") DirectorRequest request,
            @RequestPart(name = "image", required = false) org.springframework.web.multipart.MultipartFile image) {
        SuccessResponse response = this.directoryService.addDirectory(request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/directories")
    public ResponseEntity<DataResponse> getAllDirectories() {
        DataResponse response = this.directoryService.getAllDirectories();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/directory/{id}")
    public ResponseEntity<DataResponse> getDirectoryById(@PathVariable("id") Long id) {
        DataResponse response = this.directoryService.getDirectoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/directory/{id}")
    public ResponseEntity<SuccessResponse> updateDirectory(@PathVariable("id") Long id,
            @RequestPart("directory") DirectorRequest request,
            @RequestPart(name = "image", required = false) org.springframework.web.multipart.MultipartFile image) {
        SuccessResponse response = this.directoryService.updateDirectory(id, request, image);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/directory/{id}")
    public ResponseEntity<SuccessResponse> softDeleteDirectory(@PathVariable("id") Long id) {
        SuccessResponse response = this.directoryService.softDeleteDirectory(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/directory/{id}/hard")
    public ResponseEntity<SuccessResponse> deleteDirectory(@PathVariable("id") Long id) {
        SuccessResponse response = this.directoryService.deleteDirectory(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    

//------sk add media---------
    
    @PostMapping("/save-media")
    public ResponseEntity<DataResponse> addMedia(
            @RequestPart(name = "images", required = true) List<MultipartFile> images) throws Exception {
        DataResponse response = this.galleryService.createMedia(images);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @DeleteMapping("/delete-media/{id}")
    public ResponseEntity<DataResponse> deleteMedia(@PathVariable Long id) {
        DataResponse response = galleryService.deleteMedia(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
//    ------------sk add event ----
    
    @PostMapping("/add-event")
    public ResponseEntity<?> addEvent(@RequestBody Event event) {
    	
  
        return ResponseEntity.ok(eventService.saveEvent(event));
    }
    
    @PutMapping("/update-event/{id}")
    public ResponseEntity<?> updateEvent(
            @PathVariable Long id,
            @RequestBody Event eventDetails) {
        
        Event updatedEvent = eventService.updateEvent(id, eventDetails);
        return ResponseEntity.ok(updatedEvent);
    }
    
    

    @DeleteMapping("delete-event/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
      
        return   eventService.deleteEventById(id);
    }
    
    
//    sk-----------------------Imagecategories--------------------
    
    @PostMapping("/saveimgcat")
    public ResponseEntity<?> saveImageCategory(@RequestBody Imagecategory imgcat){
	
    	
    	
    	return imageCatService.save(imgcat);
    	
    }
// --------------------------------Club members-----------------------
    
    
    @PostMapping("/savemembers")
    public ResponseEntity<?> saveMembers(
            @RequestPart("members") String membersJson,
            @RequestPart("images") List<MultipartFile> images) throws Exception {

        // Parse JSON list of members
        ObjectMapper mapper = new ObjectMapper();
        List<Clubmembers> members = Arrays.asList(mapper.readValue(membersJson, Clubmembers[].class));

        List<Clubmembers> savedMembers = membersService.createMembers(members, images);
        return ResponseEntity.ok(savedMembers);
    }

    @PutMapping("/updatemember/{id}")
    public ResponseEntity<?> updateMember(
            @PathVariable Long id,
            @RequestPart("member") String memberJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Clubmembers updatedData = mapper.readValue(memberJson, Clubmembers.class);

        Clubmembers updatedMember = membersService.updateMemberById(id, updatedData, image);
        return ResponseEntity.ok(updatedMember);
    }
    
    @DeleteMapping("/deletemember")
    public ResponseEntity<?>deleteMember(@RequestParam Long id){
		return membersService.deleteMember(id);
    	
    }

    
}
