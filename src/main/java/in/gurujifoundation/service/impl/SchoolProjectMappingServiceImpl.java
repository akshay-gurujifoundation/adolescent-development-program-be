package in.gurujifoundation.service.impl;

import in.gurujifoundation.domain.*;
import in.gurujifoundation.exception.InternalServerException;
import in.gurujifoundation.mapper.SchoolProjectMappingMapper;
import in.gurujifoundation.repository.SchoolProjectMappingRepository;
import in.gurujifoundation.response.SchoolProjectMappingDetails;
import in.gurujifoundation.service.SchoolProjectMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SchoolProjectMappingServiceImpl implements SchoolProjectMappingService {

    private final SchoolProjectMappingRepository schoolProjectMappingRepository;

    @Autowired
    public SchoolProjectMappingServiceImpl(SchoolProjectMappingRepository schoolProjectMappingRepository) {
        this.schoolProjectMappingRepository = schoolProjectMappingRepository;
    }

    @Override
    public void saveSchoolProjectMapping(Project project, School school, Teacher teacher, List<Student> studentList) {
        try {
            SchoolProjectMapping schoolProjectMapping = SchoolProjectMappingMapper.INSTANCE.mapToEntity(project, school, teacher, studentList);
            schoolProjectMappingRepository.save(schoolProjectMapping);
        } catch (Exception e) {
            log.error("Error occurred while assigning school, teacher and students to project with id: {}", project.getId(), e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public List<SchoolProjectMappingDetails> getAllSchoolProjectMappings(Long schoolId) {
        List<SchoolProjectMapping> schoolProjectMappings = new ArrayList<>();
        if (schoolId == null) {
            schoolProjectMappings = schoolProjectMappingRepository.findAll();

        } else {
            schoolProjectMappings = schoolProjectMappingRepository.findBySchoolId(schoolId);
        }
        return SchoolProjectMappingMapper.INSTANCE.mapToSchoolProjectMappingDerailsList(schoolProjectMappings);

    }
}
