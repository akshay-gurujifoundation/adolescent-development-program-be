package in.gurujifoundation.service;

import in.gurujifoundation.domain.*;
import in.gurujifoundation.response.SchoolProjectMappingDetails;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public interface SchoolProjectMappingService {

    void saveSchoolProjectMapping(Project project, School school, Teacher teacher, List<Student> studentList);

    List<SchoolProjectMappingDetails> getAllSchoolProjectMappings(Long schoolId);

    SchoolProjectMapping getSchoolProjectMapping(Long id, @NotEmpty Long schoolId);

    void save(SchoolProjectMapping schoolProjectMapping);
}
