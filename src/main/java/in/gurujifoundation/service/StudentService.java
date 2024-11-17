package in.gurujifoundation.service;

import in.gurujifoundation.domain.Student;
import in.gurujifoundation.request.CreateOrUpdateStudentRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.StudentDetails;
import in.gurujifoundation.response.StudentsResponse;

import java.util.List;

public interface StudentService {
    ResponseMessage createStudent(CreateOrUpdateStudentRequest request);

    StudentDetails getStudentById(Long id);

    StudentsResponse getStudents(Long schoolId);

    ResponseMessage updateStudent(CreateOrUpdateStudentRequest createOrUpdateStudentRequest, Long id);

    ResponseMessage deleteStudent(Long id);

    Student getStudent(Long id);

    List<Student> getStudentsByIds(List<Long> studentIds);

    void updateStudents(List<Student> students);

    StudentsResponse getStudentsNotInSchoolProject(Long projectId, Long schoolId);
}
