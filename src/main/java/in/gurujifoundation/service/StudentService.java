package in.gurujifoundation.service;

import in.gurujifoundation.domain.Student;
import in.gurujifoundation.dto.BulkUploadResponse;
import in.gurujifoundation.request.CreateOrUpdateStudentRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.StudentDetails;
import in.gurujifoundation.response.StudentsResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface StudentService {
    ResponseMessage createStudent(CreateOrUpdateStudentRequest request);

    StudentDetails getStudentById(Long id);

    StudentsResponse getStudents(Long schoolId);

    ResponseMessage updateStudent(CreateOrUpdateStudentRequest createOrUpdateStudentRequest, Long id);

    ResponseMessage deleteStudent(Long id);

    Student getStudent(Long id);

    List<Student> getStudentsByIds(Set<Long> studentIds);

    void updateStudents(List<Student> students);

    StudentsResponse getStudentsNotInSchoolProject(Long projectId, Long schoolId);

    StudentsResponse getStudentsInSchoolProject(Long projectId, Long schoolId);

    Pair<HttpHeaders, InputStreamResource> getStudentExcelBySchoolId(Long schoolId);

    Pair<HttpHeaders, InputStreamResource> getStudentUploadTemplate();

    BulkUploadResponse uploadStudentExcel(Long schoolId, MultipartFile file);
}
