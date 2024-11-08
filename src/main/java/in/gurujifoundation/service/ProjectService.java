package in.gurujifoundation.service;

import in.gurujifoundation.domain.Project;
import in.gurujifoundation.request.CreateOrUpdateProjectRequest;
import in.gurujifoundation.response.ProjectDetails;
import in.gurujifoundation.response.ProjectResponse;
import in.gurujifoundation.response.ResponseMessage;
import jakarta.validation.Valid;

public interface ProjectService {
    ResponseMessage createProject(@Valid CreateOrUpdateProjectRequest createOrUpdateProjectRequest);

    ProjectDetails getProjectById(Long id);

    ResponseMessage updateProject(CreateOrUpdateProjectRequest updateProjectRequest, Long id);

    ProjectResponse getAllProjects();

    ResponseMessage deleteProject(Long id);

    Project getProject(Long id);
}
