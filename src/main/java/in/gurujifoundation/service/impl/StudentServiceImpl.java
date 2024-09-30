package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.School;
import in.gurujifoundation.domain.Student;
import in.gurujifoundation.exception.EntityNotFoundException;
import in.gurujifoundation.exception.InternalServerException;
import in.gurujifoundation.mapper.StudentMapper;
import in.gurujifoundation.repository.StudentRepository;
import in.gurujifoundation.request.CreateOrUpdateStudentRequest;
import in.gurujifoundation.response.*;
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

    private final SchoolService schoolService;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, SchoolService schoolService) {
        this.studentRepository = studentRepository;
        this.schoolService = schoolService;
    }

    @Override
    public ResponseMessage createStudent(CreateOrUpdateStudentRequest request) {
        try {
            SchoolDetails school = schoolService.getSchoolById(request.getSchoolId());
            Student student = StudentMapper.INSTANCE.toEntity(request,school);
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
    public StudentsResponse getStudents() {
        try {
            List<Student> students = studentRepository.findAll();
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
            StudentMapper.INSTANCE.updateStudent(request, student,school);
            studentRepository.save(student);
            return ResponseMessage.builder().message(ErrorCodeConstant.SCHOOL_UPDATED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while updating student with email: {}", request.getEmail(), e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    private Student getStudent(Long id) {
        log.debug("Starting to fetch student details for id: {}", id);
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) {
            log.warn("No Student found for id: {}", id);
            throw new EntityNotFoundException(ErrorCodeConstant.STUDENT_DOES_NOT_EXIST);
        }
        return studentOptional.get();
    }
}
