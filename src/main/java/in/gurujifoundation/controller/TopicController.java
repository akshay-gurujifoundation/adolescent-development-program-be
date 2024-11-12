package in.gurujifoundation.controller;

import in.gurujifoundation.request.CreateOrUpdateTopicRequest;
import in.gurujifoundation.response.APIResponse;
import in.gurujifoundation.response.TopicDetails;
import in.gurujifoundation.response.TopicResponse;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.service.TopicService;
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
@RequestMapping("/topics")
@Tag(name = "Topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @Operation(
            summary = "Save Topic",
            description = "Endpoint to save Topic",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Topic created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> createTopic(@RequestBody @Valid CreateOrUpdateTopicRequest createOrUpdateTopicRequest) {
        ResponseMessage responseMessage = topicService.createTopic(createOrUpdateTopicRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }


    @Operation(
            summary = "Update Topic",
            description = "Endpoint to update topic",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Topic updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTopic(@RequestBody @Valid CreateOrUpdateTopicRequest updateTopicRequest, @PathVariable Long id) {
        ResponseMessage responseMessage = topicService.updateTopic(updateTopicRequest, id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }

    @Operation(
            summary = "Get Topic by id",
            description = "Endpoint to retrieve topic details by id",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Topic details retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDetails.class))),
            @ApiResponse(responseCode = "404", description = "School not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })

    @GetMapping("/{id}")
    public ResponseEntity<?> getTopicById(@PathVariable Long id) {
        TopicDetails topicDetails = topicService.getTopicById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.builder().status(Boolean.TRUE).data(topicDetails).build());
    }

    @Operation(
            summary = "Get all topics",
            description = "Endpoint to retrieve all topics",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Topics retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> getAllTopics(@RequestParam(value = "projectId", required = false) Long projectId) {
        TopicResponse topicResponse = topicService.getAllTopics(projectId);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).data(topicResponse).build());
    }

    @Operation(
            summary = "Delete Topic by id",
            description = "Endpoint to delete topic by id",
            security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "OAuth Flow")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Topic deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTopic(@PathVariable Long id) {
        ResponseMessage responseMessage = topicService.deleteTopic(id);
        return ResponseEntity.ok(APIResponse.builder().status(Boolean.TRUE).messages(List.of(responseMessage)).build());
    }
}
