package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.School;
import in.gurujifoundation.domain.Teacher;
import in.gurujifoundation.exception.EntityNotFoundException;
import in.gurujifoundation.exception.InternalServerException;
import in.gurujifoundation.mapper.TeacherMapper;
import in.gurujifoundation.repository.TeacherRepository;
import in.gurujifoundation.request.CreateOrUpdateTeacherRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.TeacherDetails;
import in.gurujifoundation.response.TeacherResponse;
import in.gurujifoundation.service.SchoolService;
import in.gurujifoundation.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    private final SchoolService schoolService;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, SchoolService schoolService) {
        this.teacherRepository = teacherRepository;
        this.schoolService = schoolService;
    }

    @Override
    public ResponseMessage createTeacher(CreateOrUpdateTeacherRequest createTeacherRequest) {
        try {
            log.debug("Started saving teacher with name: {}", createTeacherRequest.getName());
            School school = schoolService.getSchool(createTeacherRequest.getSchoolId());
            Teacher teacher = TeacherMapper.INSTANCE.mapToEntity(createTeacherRequest, school);
            teacherRepository.save(teacher);
            log.debug("Successfully saved teacher with name: {}", createTeacherRequest.getName());
            return ResponseMessage.builder().message(ErrorCodeConstant.TEACHER_CREATED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while saving teacher with name: {}", createTeacherRequest.getName(), e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public TeacherDetails getTeacherById(Long id) {
        try {
            log.debug("Started fetching teacher with id: {}", id);
            Teacher teacher = getTeacher(id);
            log.debug("Successfully retrieved teacher with id: {}", id);
            return TeacherMapper.INSTANCE.mapToTeacherDetailsResponse(teacher);
        } catch (Exception e) {
            log.error("Error occurred while fetching teacher with id: {} ", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public TeacherResponse getAllTeachers() {
        try {
            log.debug("Started fetching all teachers");
            List<Teacher> teacherList = teacherRepository.findAll();
            List<TeacherDetails> teacherDetails = TeacherMapper.INSTANCE.mapToTeacherDetailsList(teacherList);
            log.debug("Successfully fetched all teachers");
            return TeacherResponse.builder().teacherDetails(teacherDetails).build();
        } catch (Exception e) {
            log.error("Error occurred while fetching all teachers", e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public ResponseMessage updateTeacher(CreateOrUpdateTeacherRequest updateTeacherRequest, Long id) {
        try {
            log.debug("Started updating teacher with id: {}", id);
            School school = schoolService.getSchool(updateTeacherRequest.getSchoolId());
            Teacher teacher = getTeacher(id);
            TeacherMapper.INSTANCE.updateTeacher(teacher, updateTeacherRequest, school);
            teacherRepository.save(teacher);
            log.debug("Successfully updated teacher with id: {}", id);
            return ResponseMessage.builder().message(ErrorCodeConstant.TEACHER_UPDATED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while updating teacher with id: {}", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public ResponseMessage deleteTeacher(Long id) {
        try {
            log.debug("Started deleting teacher with id: {}", id);
            teacherRepository.deleteById(id);
            log.debug("Successfully deleted teacher with id: {}", id);
            return ResponseMessage.builder().message(ErrorCodeConstant.TEACHER_DELETED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while deleting teacher with id: {}", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    private Teacher getTeacher(Long id) {
        log.debug("Starting to fetch teacher details for id: {}", id);
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);
        if (teacherOptional.isEmpty()) {
            log.warn("No Teacher found for id: {}", id);
            throw new EntityNotFoundException(ErrorCodeConstant.TEACHER_DOES_NOT_EXIST);
        }
        return teacherOptional.get();
    }
}
