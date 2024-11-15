package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.Project;
import in.gurujifoundation.domain.ProjectCoordinator;
import in.gurujifoundation.exception.EntityNotFoundException;
import in.gurujifoundation.exception.InternalServerException;
import in.gurujifoundation.mapper.ProjectCoordinatorMapper;
import in.gurujifoundation.repository.ProjectCoordinatorRepository;
import in.gurujifoundation.request.CreateOrUpdateProjectCoordinatorRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.ProjectCoordinatorDetails;
import in.gurujifoundation.response.ProjectCoordinatorResponse;
import in.gurujifoundation.service.ProjectService;
import in.gurujifoundation.service.ProjectCoordinatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ProjectCoordinatorServiceImpl implements ProjectCoordinatorService {

    private final ProjectCoordinatorRepository projectCoordinatorRepository;


    public ProjectCoordinatorServiceImpl(ProjectCoordinatorRepository projectCoordinatorRepository) {
        this.projectCoordinatorRepository = projectCoordinatorRepository;
    }

    @Override
    public ResponseMessage createProjectCoordinator(CreateOrUpdateProjectCoordinatorRequest createOrUpdateProjectCoordinatorRequest) {
        ProjectCoordinator projectCoordinator = ProjectCoordinatorMapper.INSTANCE.mapToEntity(createOrUpdateProjectCoordinatorRequest);
        projectCoordinatorRepository.save(projectCoordinator);
        return ResponseMessage.builder().message(ErrorCodeConstant.PROJECT_CREATED_SUCCESSFULLY).build();
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
    public ResponseMessage deleteProjectCoordinator(Long id) {
        try {
            log.debug("Started deleting project coordinator with id: {}", id);
            projectCoordinatorRepository.deleteById(id);
            log.debug("Successfully deleted project coordinator with id: {}", id);
            return ResponseMessage.builder().message(ErrorCodeConstant.PROJECT_COORDINATOR_DELETED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while deleting project coordinator with id: {}", id, e);
            throw new InternalServerException("Unexpected error occurred");
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
