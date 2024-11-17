package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.Parent;
import in.gurujifoundation.domain.School;
import in.gurujifoundation.domain.Student;
import in.gurujifoundation.exception.EntityNotFoundException;
import in.gurujifoundation.exception.InternalServerException;
import in.gurujifoundation.mapper.StudentMapper;
import in.gurujifoundation.repository.ParentRepository;
import in.gurujifoundation.repository.StudentRepository;
import in.gurujifoundation.request.CreateOrUpdateStudentRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.SchoolDetails;
import in.gurujifoundation.response.StudentDetails;
import in.gurujifoundation.response.StudentsResponse;
import in.gurujifoundation.service.SchoolService;
import in.gurujifoundation.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final ParentRepository parentRepository;

    private final SchoolService schoolService;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, ParentRepository parentRepository, SchoolService schoolService) {
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.schoolService = schoolService;
    }

    @Override
    public ResponseMessage createStudent(CreateOrUpdateStudentRequest request) {
        try {
            SchoolDetails school = schoolService.getSchoolById(request.getSchoolId());
            Student student = StudentMapper.INSTANCE.toEntity(request, school);
            Parent parent = student.getParent();
            parentRepository.save(parent);
            studentRepository.save(student);
            return ResponseMessage.builder().message(ErrorCodeConstant.STUDENT_CREATED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while saving student with email: {}", request.getName(), e);
            throw new InternalServerException("Unexpected error occurred");
        }

    }

    @Override
    public StudentDetails getStudentById(Long id) {
        try {
            Student student = getStudent(id);
            log.debug("Successfully retrieved student details for id: {}", id);
            return StudentMapper.INSTANCE.toStudentDetails(student);
        } catch (Exception e) {
            log.error("Error occurred while fetching student from db for id: {} ", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public StudentsResponse getStudents(Long schoolId) {
        try {
            List<Student> students;
            if (schoolId != null) {
                students = studentRepository.findAllBySchoolId(schoolId);
            } else {
                students = studentRepository.findAll();
            }
            List<StudentDetails> studentDetails = StudentMapper.INSTANCE.toStudentsDetails(students);
            return StudentsResponse.builder().students(studentDetails).build();
        } catch (Exception e) {
            log.error("Error occurred while fetching students from db ", e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public ResponseMessage updateStudent(CreateOrUpdateStudentRequest request, Long id) {
        try {
            Student student = getStudent(id);
            School school = schoolService.getSchool(request.getSchoolId());
            StudentMapper.INSTANCE.updateStudent(request, student, school);
            studentRepository.save(student);
            return ResponseMessage.builder().message(ErrorCodeConstant.SCHOOL_UPDATED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while updating student with email: {}", request.getEmail(), e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public ResponseMessage deleteStudent(Long id) {
        try {
            log.debug("Started deleting student with id: {}", id);
            studentRepository.deleteById(id);
            log.debug("Successfully deleted student with id: {}", id);
            return ResponseMessage.builder().message(ErrorCodeConstant.STUDENT_DELETED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while deleting student with id: {}", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public Student getStudent(Long id) {
        log.debug("Starting to fetch student details for id: {}", id);
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) {
            log.warn("No Student found for id: {}", id);
            throw new EntityNotFoundException(ErrorCodeConstant.STUDENT_DOES_NOT_EXIST);
        }
        return studentOptional.get();
    }

    @Override
    public List<Student> getStudentsByIds(List<Long> ids) {
        return studentRepository.findAllById(ids);
    }

    @Override
    public void updateStudents(List<Student> students) {
        studentRepository.saveAll(students);
    }

    @Override
    public StudentsResponse getStudentsNotInSchoolProject(Long projectId, Long schoolId) {
        try {
            List<Student> studentsNotInSchoolProject = studentRepository.findStudentsNotInProjectBySchool(schoolId, projectId);

            List<StudentDetails> studentsDetails = StudentMapper.INSTANCE.toStudentsDetails(studentsNotInSchoolProject);
            return StudentsResponse.builder().students(studentsDetails).build();
        } catch (Exception e) {
            log.error("Error occurred while fetching student which are not assigned to project with id: {} for school Id {}", projectId, schoolId, e);
            throw new InternalServerException("Unexpected error occurred");
        }

    }
}
