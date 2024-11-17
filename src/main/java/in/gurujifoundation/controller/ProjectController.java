package in.gurujifoundation.controller;

import in.gurujifoundation.request.*;
import in.gurujifoundation.response.*;
import in.gurujifoundation.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@Tag(name = "Project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(
            summary = "Save Project",
            description = "Endpoint to save Project",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody @Valid CreateOrUpdateProjectRequest createOrUpdateProjectRequest) {
        ResponseMessage responseMessage = projectService.createProject(createOrUpdateProjectRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }


    @Operation(
            summary = "Update Project",
            description = "Endpoint to update project",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@RequestBody @Valid CreateOrUpdateProjectRequest updateProjectRequest, @PathVariable Long id) {
        ResponseMessage responseMessage = projectService.updateProject(updateProjectRequest, id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Get Project by id",
            description = "Endpoint to retrieve project details by id",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project details retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectDetails.class))),
            @ApiResponse(responseCode = "404", description = "School not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        ProjectDetails projectDetails = projectService.getProjectById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.builder().status(Boolean.TRUE).data(projectDetails).build());
    }

    @Operation(
            summary = "Get all projects",
            description = "Endpoint to retrieve all projects",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> getAllProjects(@RequestParam(value = "schoolId", required = false) Long schoolId) {
        ProjectResponse projectResponse = projectService.getAllProjects(schoolId);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).data(projectResponse).build());
    }

    @Operation(
            summary = "Delete Project by id",
            description = "Endpoint to delete project by id",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        ResponseMessage responseMessage = projectService.deleteProject(id);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Allocate students to Project",
            description = "Endpoint to allocate students to project",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students allocated to project successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/{id}/school/assign")
    public ResponseEntity<?> assignProjectToStudents(@PathVariable Long id, @RequestBody ProjectAssignRequest projectAssignRequest) {
        ResponseMessage responseMessage = projectService.assignProjectToSchool(id, projectAssignRequest);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Get Project school mapping by school Id",
            description = "Endpoint to get project school mapping by school Id\"t",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve project mapping successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectSchoolMappingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/school")
    public ResponseEntity<?> getProjectSchoolMapping(@RequestParam Long schoolId) {
        ProjectSchoolMappingResponse projectSchoolMapping = projectService.getProjectSchoolMapping(schoolId);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).data(projectSchoolMapping).build());
    }

    @Operation(
            summary = "Allocate students to Project",
            description = "Endpoint to allocate students to project",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students allocated to project successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/{id}/school/unassign")
    public ResponseEntity<?> unAssignProjectToStudents(@PathVariable Long id, @RequestBody ProjectUnAssignRequest projectUnAssignRequest) {
        ResponseMessage responseMessage = projectService.unAssignProjectToSchool(id, projectUnAssignRequest);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Assign students to Project",
            description = "Endpoint to assign students to project",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students assign to project successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/{id}/schools/students/assign")
    public ResponseEntity<?> assignStudentToProject(@PathVariable Long id, @RequestBody ProjectStudentAssignUnAssignRequest projectStudentAssignUnAssignRequest) {
        ResponseMessage responseMessage = projectService.assignStudentToProject(id, projectStudentAssignUnAssignRequest);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Un-assign students to Project",
            description = "Endpoint to un-assign students to project",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students un-assign to project successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/{id}/schools/students/un-assign")
    public ResponseEntity<?> unAssignStudentToProject(@PathVariable Long id, @RequestBody ProjectStudentAssignUnAssignRequest projectStudentAssignUnAssignRequest) {
        ResponseMessage responseMessage = projectService.unAssignStudentToProject(id, projectStudentAssignUnAssignRequest);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

}
