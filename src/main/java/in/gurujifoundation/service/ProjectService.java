package in.gurujifoundation.service;

import in.gurujifoundation.domain.Project;
import in.gurujifoundation.request.*;
import in.gurujifoundation.response.*;

public interface ProjectService {
    ResponseMessage createProject(CreateOrUpdateProjectRequest createOrUpdateProjectRequest);

    ProjectDetails getProjectById(Long id);

    ResponseMessage updateProject(CreateOrUpdateProjectRequest updateProjectRequest, Long id);

    ProjectResponse getAllProjects(Long schoolId);

    ResponseMessage deleteProject(Long id);

    Project getProject(Long id);

    void saveProject(Project project);

    ResponseMessage assignProjectToSchool(Long id, ProjectAssignRequest projectAssignRequest);

    ProjectSchoolMappingResponse getProjectSchoolMapping(Long SchoolId);

    ResponseMessage unAssignProjectToSchool(Long id, ProjectUnAssignRequest projectUnAssignRequest);

    ResponseMessage assignStudentToProject(Long id, ProjectStudentAssignUnAssignRequest projectStudentAssignUnAssignRequest);

    ResponseMessage unAssignStudentToProject(Long id, ProjectStudentAssignUnAssignRequest projectStudentAssignUnAssignRequest);

    ResponseMessage deleteSchoolProjectMapping(Long id, Long schoolId);

    ResponseMessage updateSchoolProjectMapping(Long id, Long schoolId, ProjectSchoolMappingUpdateRequest projectSchoolMappingUpdateRequest);

    SchoolProjectMappingDetails getSchoolProjectMapping(Long id, Long schoolId);
}
