package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.Performance;
import in.gurujifoundation.domain.Student;
import in.gurujifoundation.domain.Topic;
import in.gurujifoundation.exception.EntityNotFoundException;
import in.gurujifoundation.exception.InternalServerException;
import in.gurujifoundation.mapper.PerformanceMapper;
import in.gurujifoundation.repository.PerformanceRepository;
import in.gurujifoundation.request.CreateOrUpdatePerformanceRequest;
import in.gurujifoundation.response.*;
import in.gurujifoundation.service.PerformanceService;
import in.gurujifoundation.service.StudentService;
import in.gurujifoundation.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PerformanceServiceImpl implements PerformanceService {

    private final StudentService studentService;

    private final PerformanceRepository performanceRepository;

    private final TopicService topicService;

    public PerformanceServiceImpl(StudentService studentService, PerformanceRepository performanceRepository, TopicService topicService) {
        this.studentService = studentService;
        this.performanceRepository = performanceRepository;
        this.topicService = topicService;
    }

    @Override
    public ResponseMessage createPerformance(CreateOrUpdatePerformanceRequest createOrUpdatePerformanceRequest) {
        try {
            log.debug("Started saving performance for student id : {}", createOrUpdatePerformanceRequest.getStudentId());
            Student student = studentService.getStudent(createOrUpdatePerformanceRequest.getStudentId());
            Topic topic = topicService.getTopic(createOrUpdatePerformanceRequest.getTopicId());
            Performance performance = PerformanceMapper.INSTANCE.mapToEntity(createOrUpdatePerformanceRequest, student, topic);
            performanceRepository.save(performance);
            log.debug("Successfully saved performance for student id : {}", createOrUpdatePerformanceRequest.getStudentId());
            return ResponseMessage.builder().message(ErrorCodeConstant.PERFORMANCE_CREATED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while saving performance for student id : {}", createOrUpdatePerformanceRequest.getStudentId(), e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public PerformanceDetails getPerformanceById(Long id) {
        try {
            log.debug("Started fetching performance with id: {}", id);
            Performance performance = getPerformance(id);
            log.debug("Successfully retrieved performance with id: {}", id);
            return PerformanceMapper.INSTANCE.mapToPerformanceDetailsResponse(performance);
        } catch (Exception e) {
            log.error("Error occurred while fetching performance with id: {} ", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public ResponseMessage updatePerformance(CreateOrUpdatePerformanceRequest createOrUpdatePerformanceRequest, Long id) {
        try {
            log.debug("Started updating performance with id: {}", id);
            Student student = studentService.getStudent(createOrUpdatePerformanceRequest.getStudentId());
            Topic topic = topicService.getTopic(createOrUpdatePerformanceRequest.getTopicId());
            Performance Performance = getPerformance(id);
            PerformanceMapper.INSTANCE.updatePerformance(Performance, createOrUpdatePerformanceRequest, student, topic);
            performanceRepository.save(Performance);
            log.debug("Successfully updated performance with id: {}", id);
            return ResponseMessage.builder().message(ErrorCodeConstant.PERFORMANCE_UPDATED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while updating Performance with id: {}", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public PerformanceResponse getAllPerformances() {
        try {
            log.debug("Started fetching all performances");
            List<Performance> performances = performanceRepository.findAll();
            List<PerformanceDetails> PerformanceDetails = PerformanceMapper.INSTANCE.mapToPerformanceDetailsList(performances);
            log.debug("Successfully fetched all performances");
            return PerformanceResponse.builder().performances(PerformanceDetails).build();
        } catch (Exception e) {
            log.error("Error occurred while fetching all performances", e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public void savePerformances(List<Performance> performanceEntries) {
        performanceRepository.saveAll(performanceEntries);
    }

    @Override
    public void deleteByStudentIdAndTopicId(Long studentId, Long topicId) {
        performanceRepository.deleteByStudentIdAndTopicId(studentId, topicId);

    }

    @Override
    public StudentPerformanceResponse getPerformancesBySchoolAndProject(Long schoolId, Long projectId) {
        List<Performance> performances = performanceRepository.findBySchoolIdAndProjectId(schoolId, projectId);

        // Group by student
        Map<Student, List<Performance>> groupedByStudent = performances.stream()
                .collect(Collectors.groupingBy(Performance::getStudent));

        // Map each student to their response
        List<StudentPerformance> studentPerformances = groupedByStudent.entrySet().stream()
                .map(entry -> mapToStudentPerformanceResponse(entry.getKey(), entry.getValue()))
                .toList();
        return StudentPerformanceResponse.builder().studentPerformances(studentPerformances).build();
    }

    @Override
    public ResponseMessage updatePerformances(Long schoolId, Long projectId, StudentPerformanceResponse updatedPerformanceRequest) {
        try {
            // Step 1: Extract student IDs from the updated request
            List<Long> studentIds = updatedPerformanceRequest.getStudentPerformances().stream()
                    .map(StudentPerformance::getStudentId)
                    .toList();

            // Step 2: Retrieve existing performances for the provided school, project, and students
            List<Performance> existingPerformances = performanceRepository.findBySchoolProjectAndStudents(schoolId, projectId, studentIds);

            // Step 3: Map performances by student ID and topic ID for quick access
            Map<Long, Map<Long, Performance>> performanceMap = existingPerformances.stream()
                    .collect(Collectors.groupingBy(
                            p -> p.getStudent().getId(),
                            Collectors.toMap(p -> p.getTopic().getId(), p -> p)
                    ));

            // Step 4: Iterate through the request to update the marks
            for (StudentPerformance studentPerformance : updatedPerformanceRequest.getStudentPerformances()) {
                Map<Long, Performance> studentPerformanceMap = performanceMap.get(studentPerformance.getStudentId());

                if (studentPerformanceMap == null) {
                    log.warn("No performance records found for student ID: {}", studentPerformance.getStudentId());
                    continue; // Skip to the next student
                }

                for (TopicPerformanceResponse topicPerformance : studentPerformance.getTopics()) {
                    Performance performance = studentPerformanceMap.get(topicPerformance.getTopicId());

                    if (performance != null) {
                        try {
                            // Update marks as per the provided data
                            if (topicPerformance.getBeforeInterventionMark() != null) {
                                performance.setBeforeInterventionMark(topicPerformance.getBeforeInterventionMark());
                            }
                            if (topicPerformance.getAfterInterventionMark() != null) {
                                performance.setAfterInterventionMark(topicPerformance.getAfterInterventionMark());
                            }
                        } catch (Exception e) {
                            log.error("Error updating performance for student ID: {}, topic ID: {}. Skipping this entry.",
                                    studentPerformance.getStudentId(), topicPerformance.getTopicId(), e);
                            // Swallow the exception and continue with the next topic
                        }
                    } else {
                        log.warn("Performance record not found for topic ID: {} and student ID: {}. Skipping.",
                                topicPerformance.getTopicId(), studentPerformance.getStudentId());
                    }
                }
            }

            // Step 5: Save all updated performances
            performanceRepository.saveAll(existingPerformances);

            // Step 6: Return success message
            return ResponseMessage.builder()
                    .message(ErrorCodeConstant.SUCCESSFULLY_UPDATED_PERFORMANCES)
                    .build();
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating performances for schoolId: {}, projectId: {}", schoolId, projectId, e);
            throw new InternalServerException("Unexpected error occurred while updating performances.");
        }
    }



    @Override
    public ResponseMessage deletePerformance(Long id) {
        try {
            log.debug("Started deleting performance with id: {}", id);
            performanceRepository.deleteById(id);
            log.debug("Successfully deleted performance with id: {}", id);
            return ResponseMessage.builder().message(ErrorCodeConstant.PERFORMANCE_DELETED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while deleting performance with id: {}", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    private Performance getPerformance(Long id) {
        log.debug("Starting to fetch performance details for id: {}", id);
        Optional<Performance> performanceOptional = performanceRepository.findById(id);
        if (performanceOptional.isEmpty()) {
            log.warn("No performance found for id: {}", id);
            throw new EntityNotFoundException(ErrorCodeConstant.PERFORMANCE_DOES_NOT_EXIST);
        }
        return performanceOptional.get();
    }

    private StudentPerformance mapToStudentPerformanceResponse(Student student, List<Performance> performances) {
        List<TopicPerformanceResponse> topics = performances.stream()
                .map(performance -> TopicPerformanceResponse.builder()
                        .topicId(performance.getTopic().getId())
                        .topicName(performance.getTopic().getName())
                        .beforeInterventionMark(performance.getBeforeInterventionMark())
                        .afterInterventionMark(performance.getAfterInterventionMark())
                        .build())
                .toList();

        return StudentPerformance.builder()
                .studentId(student.getId())
                .studentName(student.getName())
                .topics(topics)
                .build();
    }
}
