package in.gurujifoundation.controller;

import in.gurujifoundation.request.CreateOrUpdatePerformanceRequest;
import in.gurujifoundation.request.StudentPerformanceExcelDownloadRequest;
import in.gurujifoundation.response.*;
import in.gurujifoundation.service.PerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/performances")
@Tag(name = "Performance")
public class PerformanceController {

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @Operation(
            summary = "Save Performance",
            description = "Endpoint to save Performance",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Performance created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> createPerformance(@RequestBody @Valid CreateOrUpdatePerformanceRequest createOrUpdatePerformanceRequest) {
        ResponseMessage responseMessage = performanceService.createPerformance(createOrUpdatePerformanceRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }


    @Operation(
            summary = "Update Performance",
            description = "Endpoint to update performance",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Performance updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePerformance(@RequestBody @Valid CreateOrUpdatePerformanceRequest updatePerformanceRequest, @PathVariable Long id) {
        ResponseMessage responseMessage = performanceService.updatePerformance(updatePerformanceRequest, id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Get Performance by id",
            description = "Endpoint to retrieve performance details by id",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Performance details retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PerformanceDetails.class))),
            @ApiResponse(responseCode = "404", description = "School not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })

    @GetMapping("/{id}")
    public ResponseEntity<?> getPerformanceById(@PathVariable Long id) {
        PerformanceDetails performanceDetails = performanceService.getPerformanceById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.builder().status(Boolean.TRUE).data(performanceDetails).build());
    }

    @Operation(
            summary = "Get all performances",
            description = "Endpoint to retrieve all performances",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Performances retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PerformanceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> getAllPerformances() {
        PerformanceResponse performanceResponse = performanceService.getAllPerformances();
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).data(performanceResponse).build());
    }

    @Operation(
            summary = "Delete Performance by id",
            description = "Endpoint to delete performance by id",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Performance deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerformance(@PathVariable Long id) {
        ResponseMessage responseMessage = performanceService.deletePerformance(id);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Get student performance by school and project id performances",
            description = "Endpoint to retrieve performances by school and project ids",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Performances retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentPerformanceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/students")
    public ResponseEntity<?> getPerformances(
            @RequestParam Long schoolId,
            @RequestParam Long projectId) {
        StudentPerformanceResponse studentPerformanceResponse = performanceService.getPerformancesBySchoolAndProject(schoolId, projectId);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).data(studentPerformanceResponse).build());
    }


    @Operation(
            summary = "Get student performance by school and project id performances",
            description = "Endpoint to retrieve performances by school and project ids",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Performances retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentPerformanceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/students")
    public ResponseEntity<?> updatePerformances(@RequestParam Long schoolId, @RequestParam Long projectId, @RequestBody StudentPerformanceResponse updatedPerformanceRequest) {
        ResponseMessage responseMessage = performanceService.updatePerformances(schoolId, projectId, updatedPerformanceRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Get student performance upload template",
            description = "Endpoint to retrieve student performance upload template",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "student performance upload template retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PostMapping(value = "/download")
    public ResponseEntity<?> downloadStudentPerformanceExcel(@RequestBody @Valid StudentPerformanceExcelDownloadRequest studentPerformanceExcelDownloadRequest) {
        Pair<HttpHeaders, InputStreamResource> candidateResultCsvHeaderPair = performanceService.downloadStudentPerformanceExcel(studentPerformanceExcelDownloadRequest);
        return new ResponseEntity<>(candidateResultCsvHeaderPair.getValue(), candidateResultCsvHeaderPair.getKey(), HttpStatus.OK);
    }

    @Operation(
            summary = "Get student performance upload template",
            description = "Endpoint to retrieve student performance upload template",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "student performance upload template retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadStudentPerformanceExcel(@RequestPart MultipartFile file) {
        ResponseMessage responseMessage = performanceService.uploadStudentPerformanceExcel(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }
}
