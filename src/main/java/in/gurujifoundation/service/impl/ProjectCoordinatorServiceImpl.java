package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.ProjectCoordinator;
import in.gurujifoundation.domain.User;
import in.gurujifoundation.domain.UserRole;
import in.gurujifoundation.dto.CreateUserRequest;
import in.gurujifoundation.exception.*;
import in.gurujifoundation.mapper.ProjectCoordinatorMapper;
import in.gurujifoundation.repository.ProjectCoordinatorRepository;
import in.gurujifoundation.request.CreateOrUpdateProjectCoordinatorRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.ProjectCoordinatorDetails;
import in.gurujifoundation.response.ProjectCoordinatorResponse;
import in.gurujifoundation.service.ProjectCoordinatorService;
import in.gurujifoundation.service.UserService;
import in.gurujifoundation.validator.ProjectCoordinatorValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ProjectCoordinatorServiceImpl implements ProjectCoordinatorService {

    private final ProjectCoordinatorRepository projectCoordinatorRepository;
    private final UserService userService;
    private final ProjectCoordinatorValidator projectCoordinatorValidator;


    public ProjectCoordinatorServiceImpl(ProjectCoordinatorRepository projectCoordinatorRepository, UserService userService, ProjectCoordinatorValidator projectCoordinatorValidator) {
        this.projectCoordinatorRepository = projectCoordinatorRepository;
        this.userService = userService;
        this.projectCoordinatorValidator = projectCoordinatorValidator;
    }

    @Override
    @Transactional
    public ResponseMessage createProjectCoordinator(@Valid CreateOrUpdateProjectCoordinatorRequest request) {
        log.debug("Creating new project coordinator with email: {}", request.getEmail());

        projectCoordinatorValidator.validateProjectCoordinatorRequest(request);

        try {
            ProjectCoordinator projectCoordinator = ProjectCoordinatorMapper.INSTANCE.mapToEntity(request);
            User user = userService.createAndReturnUser(CreateUserRequest.builder()
                    .email(request.getEmail())
                    .password(request.getPassword())
                    .userRole(UserRole.PROJECT_COORDINATOR)
                    .build());

            projectCoordinator.setUser(user);

            projectCoordinatorRepository.save(projectCoordinator);

            log.info("Successfully created project coordinator with email: {}", request.getEmail());
            return ResponseMessage.builder()
                    .message(ErrorCodeConstant.PROJECT_COORDINATOR_CREATED_SUCCESSFULLY)
                    .build();
        } catch (Exception e) {
            log.error("Failed to create project coordinator", e);
            throw new ProjectCoordinatorCreationException("Failed to create project coordinator", e);
        }
    }

    @Override
    public ResponseMessage updateProjectCoordinator(CreateOrUpdateProjectCoordinatorRequest createOrUpdateProjectCoordinatorRequest, Long id) {
        ProjectCoordinator projectCoordinator = getProjectCoordinator(id);
        ProjectCoordinatorMapper.INSTANCE.updateEntity(projectCoordinator, createOrUpdateProjectCoordinatorRequest);
        projectCoordinatorRepository.save(projectCoordinator);
        return ResponseMessage.builder().message(ErrorCodeConstant.PROJECT_COORDINATOR_UPDATED_SUCCESSFULLY).build();
    }

    @Override
    public ProjectCoordinatorDetails getProjectCoordinatorById(Long id) {
        try {
            log.debug("Started fetching project coordinator with id: {}", id);
            ProjectCoordinator projectCoordinator = getProjectCoordinator(id);
            log.debug("Successfully retrieved project coordinator with id: {}", id);
            return ProjectCoordinatorMapper.INSTANCE.mapToProjectCoordinatorDetailsResponse(projectCoordinator);
        } catch (Exception e) {
            log.error("Error occurred while fetching project coordinator with id: {} ", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public ProjectCoordinatorResponse getAllProjectCoordinators() {
        try {
            log.debug("Started fetching all project coordinators");
            List<ProjectCoordinator> projectCoordinators = projectCoordinatorRepository.findAll();
            List<ProjectCoordinatorDetails> projectCoordinatorDetails = ProjectCoordinatorMapper.INSTANCE.mapToProjectCoordinatorDetailsList(projectCoordinators);
            log.debug("Successfully fetched all project coordinators");
            return ProjectCoordinatorResponse.builder().projectCoordinators(projectCoordinatorDetails).build();
        } catch (Exception e) {
            log.error("Error occurred while fetching all project coordinators", e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    @Transactional
    public ResponseMessage deleteProjectCoordinator(@NotNull Long id) {
        log.debug("Deleting project coordinator with id: {}", id);

        try {
            ProjectCoordinator projectCoordinator = getProjectCoordinator(id);

            // User deletion will be handled automatically through cascade configuration in entity
            log.debug("User will be deleted automatically through cascade for project coordinator id: {}", id);

            projectCoordinatorRepository.deleteById(id);
            log.info("Successfully deleted project coordinator with id: {}", id);

            return ResponseMessage.builder()
                    .message(ErrorCodeConstant.PROJECT_COORDINATOR_DELETED_SUCCESSFULLY)
                    .build();
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to delete project coordinator with id: {}", id, e);
            throw new ProjectCoordinatorDeletionException("Failed to delete project coordinator", e);
        }
    }

    public ProjectCoordinator getProjectCoordinator(Long id) {
        Optional<ProjectCoordinator> projectCoordinatorOptional = projectCoordinatorRepository.findById(id);
        if (projectCoordinatorOptional.isEmpty()) {
            throw new EntityNotFoundException("ProjectCoordinator with id " + id + " not found");
        }
        return projectCoordinatorOptional.get();
    }

    @Override
    public Set<ProjectCoordinator> getProjectCoordinators(List<Long> projectCoordinatorIds) {
        List<ProjectCoordinator> projectCoordinators = projectCoordinatorRepository.findAllById(projectCoordinatorIds);
        return new HashSet<>(projectCoordinators);
    }

    @Override
    public void saveProjectCoordinators(Set<ProjectCoordinator> projectCoordinators) {
        projectCoordinatorRepository.saveAll(projectCoordinators);
    }
}
