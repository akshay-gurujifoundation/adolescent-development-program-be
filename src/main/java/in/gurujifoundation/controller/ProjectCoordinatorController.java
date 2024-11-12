package in.gurujifoundation.controller;

import in.gurujifoundation.request.CreateOrUpdateProjectCoordinatorRequest;
import in.gurujifoundation.response.APIResponse;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.ProjectCoordinatorDetails;
import in.gurujifoundation.response.ProjectCoordinatorResponse;
import in.gurujifoundation.service.ProjectCoordinatorService;
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
@RequestMapping("/project-coordinators")
@Tag(name = "Project Coordinators")
public class ProjectCoordinatorController {

    private final ProjectCoordinatorService projectCoordinatorService;

    public ProjectCoordinatorController(ProjectCoordinatorService projectCoordinatorService) {
        this.projectCoordinatorService = projectCoordinatorService;
    }

    @Operation(
            summary = "Save Project Coordinator",
            description = "Endpoint to save Project Coordinator",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project Coordinator created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> createProjectCoordinator(@RequestBody @Valid CreateOrUpdateProjectCoordinatorRequest createOrUpdateProjectCoordinatorRequest) {
        ResponseMessage responseMessage = projectCoordinatorService.createProjectCoordinator(createOrUpdateProjectCoordinatorRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }


    @Operation(
            summary = "Update ProjectCoordinator",
            description = "Endpoint to update projectCoordinator",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "ProjectCoordinator updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProjectCoordinator(@RequestBody @Valid CreateOrUpdateProjectCoordinatorRequest updateProjectCoordinatorRequest, @PathVariable Long id) {
        ResponseMessage responseMessage = projectCoordinatorService.updateProjectCoordinator(updateProjectCoordinatorRequest, id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Get ProjectCoordinator by id",
            description = "Endpoint to retrieve projectCoordinator details by id",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProjectCoordinator details retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectCoordinatorDetails.class))),
            @ApiResponse(responseCode = "404", description = "School not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectCoordinatorById(@PathVariable Long id) {
        ProjectCoordinatorDetails projectCoordinatorDetails = projectCoordinatorService.getProjectCoordinatorById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.builder().status(Boolean.TRUE).data(projectCoordinatorDetails).build());
    }

    @Operation(
            summary = "Get all projectCoordinators",
            description = "Endpoint to retrieve all projectCoordinators",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProjectCoordinators retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectCoordinatorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> getAllProjectCoordinators() {
        ProjectCoordinatorResponse projectCoordinatorResponse = projectCoordinatorService.getAllProjectCoordinators();
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).data(projectCoordinatorResponse).build());
    }

    @Operation(
            summary = "Delete ProjectCoordinator by id",
            description = "Endpoint to delete projectCoordinator by id",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProjectCoordinator deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProjectCoordinator(@PathVariable Long id) {
        ResponseMessage responseMessage = projectCoordinatorService.deleteProjectCoordinator(id);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }
}
