package in.gurujifoundation.service;

import in.gurujifoundation.domain.ProjectCoordinator;
import in.gurujifoundation.request.CreateOrUpdateProjectCoordinatorRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.ProjectCoordinatorDetails;
import in.gurujifoundation.response.ProjectCoordinatorResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

public interface ProjectCoordinatorService {
    ResponseMessage createProjectCoordinator(CreateOrUpdateProjectCoordinatorRequest createOrUpdateProjectCoordinatorRequest);

    ResponseMessage updateProjectCoordinator(CreateOrUpdateProjectCoordinatorRequest createOrUpdateProjectCoordinatorRequest, Long id);

    ProjectCoordinatorDetails getProjectCoordinatorById(Long id);

    ProjectCoordinatorResponse getAllProjectCoordinators();

    ResponseMessage deleteProjectCoordinator(Long id);

    ProjectCoordinator getProjectCoordinator(Long id);

    Set<ProjectCoordinator> getProjectCoordinators(List<Long> projectCoordinatorIds);

    void saveProjectCoordinators(Set<ProjectCoordinator> projectCoordinators);
}
