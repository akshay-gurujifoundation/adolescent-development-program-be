package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.School;
import in.gurujifoundation.exception.EntityNotFoundException;
import in.gurujifoundation.exception.InternalServerException;
import in.gurujifoundation.mapper.SchoolMapper;
import in.gurujifoundation.repository.SchoolRepository;
import in.gurujifoundation.request.CreateOrUpdateSchoolRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.SchoolDetails;
import in.gurujifoundation.response.SchoolsResponse;
import in.gurujifoundation.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    @Autowired
    public SchoolServiceImpl(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @Override
    public ResponseMessage createSchool(CreateOrUpdateSchoolRequest createSchoolRequest) {
        try {
            log.debug("Started creating school with name: {}", createSchoolRequest.getName());
            School school = SchoolMapper.INSTANCE.mapToEntity(createSchoolRequest);
            schoolRepository.save(school);
            log.debug("Successfully created school with name: {}", createSchoolRequest.getName());
            return ResponseMessage.builder().message(ErrorCodeConstant.SCHOOL_CREATED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while saving school with name: {}", createSchoolRequest.getName(), e);
            throw new InternalServerException("Unexpected error occurred");
        }

    }

    @Override
    public SchoolDetails getSchoolById(Long id) {
        try {
            log.debug("Started fetching school with id: {}", id);
            School school = getSchool(id);
            log.debug("Successfully retrieved school details for id: {}", id);
            return SchoolMapper.INSTANCE.mapToSchoolDetails(school);
        } catch (Exception e) {
            log.error("Error occurred while fetching school from db for id: {} ", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public SchoolsResponse getSchools() {
        try {
            log.debug("Started fetching all schools");
            List<School> schools = schoolRepository.findAll();
            List<SchoolDetails> schoolDetails = SchoolMapper.INSTANCE.mapToSchoolDetailsList(schools);
            log.debug("Successfully fetched all schools");
            return SchoolsResponse.builder().students(schoolDetails).build();
        } catch (Exception e) {
            log.error("Error occurred while fetching schools from db ", e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public ResponseMessage updateSchool(CreateOrUpdateSchoolRequest updateSchoolRequest, Long id) {
        try {
            log.debug("Started updating school with id: {}", id);
            School school = getSchool(id);
            SchoolMapper.INSTANCE.updateSchool(updateSchoolRequest, school);
            schoolRepository.save(school);
            log.debug("Successfully updated school with id: {}", id);
            return ResponseMessage.builder().message(ErrorCodeConstant.SCHOOL_UPDATED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while updating school with email: {}", updateSchoolRequest.getName(), e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public School getSchool(Long id) {
        log.debug("Starting to fetch school details for id: {}", id);
        Optional<School> schoolOptional = schoolRepository.findById(id);
        if (schoolOptional.isEmpty()) {
            log.warn("No School found for id: {}", id);
            throw new EntityNotFoundException(ErrorCodeConstant.SCHOOL_DOES_NOT_EXIST);
        }
        return schoolOptional.get();
    }

    @Override
    public ResponseMessage deleteSchool(Long id) {
        try {
            log.debug("Started deleting school with id: {}", id);
            schoolRepository.deleteById(id);
            log.debug("Successfully deleted school with id: {}", id);
            return ResponseMessage.builder().message(ErrorCodeConstant.SCHOOL_DELETED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while deleting teacher with id: {}", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }
}
