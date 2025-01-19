package in.gurujifoundation.controller;

import in.gurujifoundation.dto.BulkUploadResponse;
import in.gurujifoundation.request.CreateOrUpdateSchoolRequest;
import in.gurujifoundation.response.APIResponse;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.SchoolDetails;
import in.gurujifoundation.response.SchoolsResponse;
import in.gurujifoundation.service.SchoolService;
import in.gurujifoundation.utils.ExcelUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/schools")
@Tag(name = "School")
@Slf4j
public class SchoolController {

    private final SchoolService schoolService;

    @Autowired
    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @Operation(
            summary = "Save School",
            description = "Endpoint to save school",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "School created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> createSchool(@RequestBody @Valid CreateOrUpdateSchoolRequest createSchoolRequest) {
        ResponseMessage responseMessage = schoolService.createSchool(createSchoolRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Update School",
            description = "Endpoint to update school",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "School updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchool(@RequestBody @Valid CreateOrUpdateSchoolRequest updateSchoolRequest, @PathVariable Long id) {
        ResponseMessage responseMessage = schoolService.updateSchool(updateSchoolRequest, id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Get School by id",
            description = "Endpoint to retrieve school details by id",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "School details retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchoolDetails.class))),
            @ApiResponse(responseCode = "404", description = "School not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })

    @GetMapping("/{id}")
    public ResponseEntity<?> getSchoolById(@PathVariable Long id) {
        SchoolDetails schoolDetails = schoolService.getSchoolById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.builder().status(Boolean.TRUE).data(schoolDetails).build());
    }

    @Operation(
            summary = "Get all schools",
            description = "Endpoint to retrieve all schools",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schools retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchoolsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> getSchools() {
        SchoolsResponse schoolsResponse = schoolService.getSchools();
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).data(schoolsResponse).build());
    }

    @Operation(
            summary = "Get all unassign schools from project",
            description = "Endpoint to retrieve unassign schools from project",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schools retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchoolsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/projects/un-assign")
    public ResponseEntity<?> getUnAssignSchoolsForProject(@RequestParam Long projectId) {
        SchoolsResponse schoolsResponse = schoolService.getUnAssignSchoolsForProject(projectId);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).data(schoolsResponse).build());
    }

    @Operation(
            summary = "Delete school by id",
            description = "Endpoint to delete school by id",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "School deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
        ResponseMessage responseMessage = schoolService.deleteSchool(id);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Get upload template for school",
            description = "Endpoint to retrieve Excel file template to upload school details",
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
    public ResponseEntity<?> getSchoolUploadTemplate() {
        Pair<HttpHeaders, InputStreamResource> schoolTemplateResult = schoolService.getSchoolUploadTemplate();
        return new ResponseEntity<>(schoolTemplateResult.getValue(), schoolTemplateResult.getKey(), HttpStatus.OK);
    }

    @Operation(summary = "Upload schools via Excel file",
            description = "Upload multiple schools using Excel file format")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schools uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or file format"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadSchoolExcel(@RequestPart MultipartFile file) {

        log.info("Received request to upload schools");

        if (!ExcelUtils.isValidExcelFile(file)) {
            return ResponseEntity.badRequest()
                    .body(APIResponse.builder()
                            .status(false)
                            .messages(List.of(new ResponseMessage("Invalid file format. Please upload an Excel file (.xlsx)")))
                            .build());
        }

        BulkUploadResponse response = schoolService.uploadSchoolExcel(file);

        return ResponseEntity.ok(APIResponse.builder()
                .status(true)
                .messages(response.getMessages())
                .data(response.getStats())
                .build());
    }

}
