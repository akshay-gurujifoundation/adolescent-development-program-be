package in.gurujifoundation.service;

import in.gurujifoundation.domain.Project;
import in.gurujifoundation.domain.School;
import in.gurujifoundation.domain.Student;
import in.gurujifoundation.domain.Teacher;

import java.util.List;

public interface SchoolProjectMappingService {

    void saveSchoolProjectMapping(Project project, School school, Teacher teacher, List<Student> studentList);
}
