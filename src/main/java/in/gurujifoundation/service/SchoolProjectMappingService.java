package in.gurujifoundation.service;

import in.gurujifoundation.domain.*;
import in.gurujifoundation.request.ProjectAssignRequest;
import in.gurujifoundation.response.SchoolProjectMappingDetails;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Set;

public interface SchoolProjectMappingService {

    void saveSchoolProjectMapping(Project project, School school, Teacher teacher, List<Student> studentList, ProjectAssignRequest projectAssignRequest);

    List<SchoolProjectMappingDetails> getAllSchoolProjectMappings(Long schoolId);

    SchoolProjectMapping getSchoolProjectMapping(Long id, @NotEmpty Long schoolId);

    void save(SchoolProjectMapping schoolProjectMapping);

    void deleteSchoolProjectMappings(Set<SchoolProjectMapping> schoolProjects);
}
