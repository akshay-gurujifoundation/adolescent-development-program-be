package in.gurujifoundation.service;

import in.gurujifoundation.domain.Student;
import in.gurujifoundation.request.CreateOrUpdateSchoolRequest;
import in.gurujifoundation.request.CreateOrUpdateStudentRequest;
import in.gurujifoundation.response.*;

public interface StudentService {
    ResponseMessage createStudent(CreateOrUpdateStudentRequest request);

    StudentDetails getStudentById(Long id);

    StudentsResponse getStudents();

    ResponseMessage updateStudent(CreateOrUpdateStudentRequest createOrUpdateStudentRequest, Long id);

    ResponseMessage deleteStudent(Long id);

    Student getStudent(Long id);
}
