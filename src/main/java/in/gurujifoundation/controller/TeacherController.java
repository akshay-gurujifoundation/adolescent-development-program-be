package in.gurujifoundation.controller;

import in.gurujifoundation.request.CreateOrUpdateTeacherRequest;
import in.gurujifoundation.response.APIResponse;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.TeacherDetails;
import in.gurujifoundation.response.TeacherResponse;
import in.gurujifoundation.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
@Tag(name = "Teacher")
public class TeacherController {

    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Operation(
            summary = "Save Teacher",
            description = "Endpoint to save teacher",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Teacher created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> createTeacher(@RequestBody @Valid CreateOrUpdateTeacherRequest createTeacherRequest) {
        ResponseMessage responseMessage = teacherService.createTeacher(createTeacherRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Update Teacher",
            description = "Endpoint to update teacher",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Teacher updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeacher(@RequestBody @Valid CreateOrUpdateTeacherRequest updateTeacherRequest, @PathVariable Long id) {
        ResponseMessage responseMessage = teacherService.updateTeacher(updateTeacherRequest, id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Get Teacher by ID",
            description = "Endpoint to retrieve teacher details by id",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher details retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherDetails.class))),
            @ApiResponse(responseCode = "404", description = "School not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable Long id) {
        TeacherDetails teacherDetails = teacherService.getTeacherById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.builder().status(Boolean.TRUE).data(teacherDetails).build());
    }

    @Operation(
            summary = "Get all teachers",
            description = "Endpoint to retrieve all teachers",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teachers retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> getAllTeachers() {
        TeacherResponse teacherResponse = teacherService.getAllTeachers();
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).data(teacherResponse).build());
    }

    @Operation(
            summary = "Delete School by id",
            description = "Endpoint to delete teacher by id",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
        ResponseMessage responseMessage = teacherService.deleteTeacher(id);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }
}
