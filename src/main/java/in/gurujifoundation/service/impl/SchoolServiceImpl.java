package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.School;
import in.gurujifoundation.exception.EntityNotFoundException;
import in.gurujifoundation.exception.InternalServerException;
import in.gurujifoundation.mapper.SchoolMapper;
import in.gurujifoundation.repository.SchoolRepository;
import in.gurujifoundation.request.CreateSchoolRequest;
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
    public ResponseMessage createSchool(CreateSchoolRequest request) {
        try {
            School school = SchoolMapper.INSTANCE.toEntity(request);
            schoolRepository.save(school);
            return ResponseMessage.builder().message(ErrorCodeConstant.SCHOOL_SAVED).build();
        } catch (Exception e) {
            log.error("Error occurred while saving school with email: {}", request.getName(), e);
            throw new InternalServerException("Unexpected error occurred");
        }

    }

    @Override
    public SchoolDetails getSchoolById(Long id) {
        try {
            log.debug("Starting to fetch school details for id: {}", id);
            Optional<School> schoolOptional = schoolRepository.findById(id);
            if (schoolOptional.isEmpty()) {
                log.warn("No School found for id: {}", id);
                throw new EntityNotFoundException(ErrorCodeConstant.SCHOOL_DOES_NOT_EXIST);
            }
            School school = schoolOptional.get();
            log.debug("Successfully retrieved school details for id: {}", id);
            return SchoolMapper.INSTANCE.toResponse(school);
        } catch (Exception e) {
            log.error("Error occurred while fetching school from db for id: {} ", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public SchoolsResponse getSchools() {
        List<School> schools = schoolRepository.findAll();
        List<SchoolDetails> schoolDetails = SchoolMapper.INSTANCE.toSchoolDetails(schools);
        return SchoolsResponse.builder().users(schoolDetails).build();

    }
}
