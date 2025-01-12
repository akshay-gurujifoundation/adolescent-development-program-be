package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.*;
import in.gurujifoundation.exception.EntityNotFoundException;
import in.gurujifoundation.exception.InternalServerException;
import in.gurujifoundation.mapper.SchoolProjectMappingMapper;
import in.gurujifoundation.repository.SchoolProjectMappingRepository;
import in.gurujifoundation.request.ProjectAssignRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.SchoolProjectMappingDetails;
import in.gurujifoundation.service.SchoolProjectMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class SchoolProjectMappingServiceImpl implements SchoolProjectMappingService {

    private final SchoolProjectMappingRepository schoolProjectMappingRepository;

    @Autowired
    public SchoolProjectMappingServiceImpl(SchoolProjectMappingRepository schoolProjectMappingRepository) {
        this.schoolProjectMappingRepository = schoolProjectMappingRepository;
    }

    @Override
    public void saveSchoolProjectMapping(Project project, School school, Teacher teacher, List<Student> studentList, ProjectAssignRequest projectAssignRequest) {
        try {
            SchoolProjectMapping schoolProjectMapping = SchoolProjectMappingMapper.INSTANCE.mapToEntity(project, school, teacher, studentList, projectAssignRequest);
            schoolProjectMappingRepository.save(schoolProjectMapping);
        } catch (Exception e) {
            log.error("Error occurred while assigning school, teacher and students to project with id: {}", project.getId(), e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public List<SchoolProjectMappingDetails> getSchoolProjectMappingsBySchoolId(Long schoolId) {
        List<SchoolProjectMapping> schoolProjectMappings = new ArrayList<>();
        if (schoolId == null) {
            schoolProjectMappings = schoolProjectMappingRepository.findAll();

        } else {
            schoolProjectMappings = schoolProjectMappingRepository.findBySchoolId(schoolId);
        }
        return SchoolProjectMappingMapper.INSTANCE.mapToSchoolProjectMappingDerailsList(schoolProjectMappings);

    }

    @Override
    public SchoolProjectMapping getSchoolProjectMapping(Long id, Long schoolId) {
        Optional<SchoolProjectMapping> schoolProjectMappingOpt = schoolProjectMappingRepository.findByProjectIdAndSchoolId(id, schoolId);
        if (schoolProjectMappingOpt.isEmpty()) {
            throw new EntityNotFoundException("School project mapping with id " + id + " not found");
        }
        return schoolProjectMappingOpt.get();
    }

    @Override
    public void save(SchoolProjectMapping schoolProjectMapping) {
        schoolProjectMappingRepository.save(schoolProjectMapping);
    }

    @Override
    public void deleteSchoolProjectMappings(Set<SchoolProjectMapping> schoolProjects) {
        schoolProjectMappingRepository.deleteAll(schoolProjects);
    }

    @Override
    public ResponseMessage deleteSchoolProjectMapping(Long projectId, Long schoolId) {
        schoolProjectMappingRepository.deleteByProjectIdAndSchoolId(projectId, schoolId);
        return ResponseMessage.builder().message(ErrorCodeConstant.SUCCESSFULLY_DELETED_SCHOOL_PROJECT_MAPPING).build();
    }

    @Override
    public SchoolProjectMapping getSchoolProjectMappingBySchoolIdAndProjectId(Long schoolId, Long projectId) {
        Optional<SchoolProjectMapping> schoolProjectMappingOptional = schoolProjectMappingRepository.findBySchoolIdAndProjectId(schoolId, projectId);
        if (schoolProjectMappingOptional.isEmpty()) {
            log.error("School project mapping with id {} not found", schoolId);
            throw new EntityNotFoundException("School project mapping not found");
        }
        return schoolProjectMappingOptional.get();
    }
}
