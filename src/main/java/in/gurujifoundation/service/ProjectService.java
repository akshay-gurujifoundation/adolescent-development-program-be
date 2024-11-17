package in.gurujifoundation.service;

import in.gurujifoundation.domain.Project;
import in.gurujifoundation.request.CreateOrUpdateProjectRequest;
import in.gurujifoundation.request.ProjectAssignRequest;
import in.gurujifoundation.request.ProjectStudentAllocationDeAllocationRequest;
import in.gurujifoundation.response.ProjectSchoolMappingResponse;
import in.gurujifoundation.response.ProjectDetails;
import in.gurujifoundation.response.ProjectResponse;
import in.gurujifoundation.response.ResponseMessage;

public interface ProjectService {
    ResponseMessage createProject(CreateOrUpdateProjectRequest createOrUpdateProjectRequest);

    ProjectDetails getProjectById(Long id);

    ResponseMessage updateProject(CreateOrUpdateProjectRequest updateProjectRequest, Long id);

    ProjectResponse getAllProjects(Long schoolId);

    ResponseMessage deleteProject(Long id);

    Project getProject(Long id);

    ResponseMessage allocateProjectToStudents(Long id, ProjectStudentAllocationDeAllocationRequest projectStudentAllocationDeAllocationRequest);

    ResponseMessage deallocateProjectToStudents(Long id, ProjectStudentAllocationDeAllocationRequest projectStudentAllocationDeAllocationRequest);

    void saveProject(Project project);

    ResponseMessage assignProjectToSchool(Long id, ProjectAssignRequest projectAssignRequest);

    ProjectSchoolMappingResponse getProjectSchoolMapping(Long SchoolId);
}
