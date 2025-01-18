package in.gurujifoundation.controller;

import in.gurujifoundation.dto.BulkUploadResponse;
import in.gurujifoundation.request.CreateOrUpdateStudentRequest;
import in.gurujifoundation.response.APIResponse;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.StudentDetails;
import in.gurujifoundation.response.StudentsResponse;
import in.gurujifoundation.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/students")
@Tag(name = "Student")
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(
            summary = "Save student",
            description = "Endpoint to save student",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody @Valid CreateOrUpdateStudentRequest request) {
        ResponseMessage responseMessage = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Update student",
            description = "Endpoint to update student",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "student updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@RequestBody @Valid CreateOrUpdateStudentRequest createOrUpdatestudentRequest, @PathVariable Long id) {
        ResponseMessage responseMessage = studentService.updateStudent(createOrUpdatestudentRequest, id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Get student by ID",
            description = "Endpoint to retrieve student details by student ID",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "student details retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDetails.class))),
            @ApiResponse(responseCode = "404", description = "student not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        StudentDetails studentDetails = studentService.getStudentById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.builder().status(Boolean.TRUE).data(studentDetails).build());
    }

    @Operation(
            summary = "Get all students",
            description = "Endpoint to retrieve student records",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "students retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getStudents(@RequestParam(value = "schoolId", required = false) Long schoolId) {
        StudentsResponse studentsResponse = studentService.getStudents(schoolId);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).data(studentsResponse).build());
    }

    @Operation(
            summary = "Delete Student by id",
            description = "Endpoint to delete student by id",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        ResponseMessage responseMessage = studentService.deleteStudent(id);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Get all unassigned students of project for school",
            description = "Endpoint to retrieve unassigned students of project for school",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "students retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("projects/unassigned-students")
    public ResponseEntity<?> getStudentsNotInSchoolProject(@RequestParam(value = "projectId") Long projectId, @RequestParam(value = "schoolId") Long schoolId) {
        StudentsResponse studentsResponse = studentService.getStudentsNotInSchoolProject(projectId, schoolId);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).data(studentsResponse).build());
    }


    @Operation(
            summary = "Get all assigned students of project for school",
            description = "Endpoint to retrieve assigned students of project for school",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "students retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("projects/assigned-students")
    public ResponseEntity<?> getStudentsInSchoolProject(@RequestParam(value = "projectId") Long projectId, @RequestParam(value = "schoolId") Long schoolId) {
        StudentsResponse studentsResponse = studentService.getStudentsInSchoolProject(projectId, schoolId);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).data(studentsResponse).build());
    }

    @Operation(
            summary = "Get all assigned students of project for school",
            description = "Endpoint to retrieve assigned students of project for school",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "students retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(value = "/download")
    public ResponseEntity<?> getStudentExcelBySchoolId(@RequestParam Long schoolId) {
        Pair<HttpHeaders, InputStreamResource> candidateResultCsvHeaderPair = studentService.getStudentExcelBySchoolId(schoolId);
        return new ResponseEntity<>(candidateResultCsvHeaderPair.getValue(), candidateResultCsvHeaderPair.getKey(), HttpStatus.OK);

    }

    @Operation(
            summary = "Get upload template for student",
            description = "Endpoint to retrieve excel file template to upload the students",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload template retrieved successfully",
                    content = @Content(mediaType = "application/vnd.ms-excel", schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(value = "/download-template")
    public ResponseEntity<?> getStudentUploadTemplate() {
        Pair<HttpHeaders, InputStreamResource> candidateResultCsvHeaderPair = studentService.getStudentUploadTemplate();
        return new ResponseEntity<>(candidateResultCsvHeaderPair.getValue(), candidateResultCsvHeaderPair.getKey(), HttpStatus.OK);

    }

    @Operation(summary = "Upload students via Excel file",
            description = "Upload multiple students using Excel file format for a specific school")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or file format"),
            @ApiResponse(responseCode = "404", description = "School not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadStudentExcel( @Parameter(description = "School ID", required = true)
                                                     @RequestParam @Valid @Positive(message = "School ID must be positive") Long schoolId,
                                                 @RequestPart MultipartFile file) {

        log.info("Received request to upload students for school ID: {}", schoolId);

        if (!isValidExcelFile(file)) {
            return ResponseEntity.badRequest()
                    .body(APIResponse.builder()
                            .status(false)
                            .messages(List.of(new ResponseMessage("Invalid file format. Please upload an Excel file (.xlsx)")))
                            .build());
        }

        BulkUploadResponse response = studentService.uploadStudentExcel(schoolId, file);

        return ResponseEntity.ok(APIResponse.builder()
                .status(true)
                .messages(response.getMessages())
                .data(response.getStats())
                .build());
    }

    private boolean isValidExcelFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
                contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || // .xlsx
                        contentType.equals("application/vnd.ms-excel")); // .xls
    }

}
