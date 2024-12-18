package in.gurujifoundation.service;

import in.gurujifoundation.domain.Performance;
import in.gurujifoundation.request.CreateOrUpdatePerformanceRequest;
import in.gurujifoundation.response.*;
import jakarta.validation.Valid;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface PerformanceService {
    ResponseMessage createPerformance(@Valid CreateOrUpdatePerformanceRequest createOrUpdatePerformanceRequest);

    ResponseMessage updatePerformance(@Valid CreateOrUpdatePerformanceRequest updatePerformanceRequest, Long id);

    PerformanceDetails getPerformanceById(Long id);

    ResponseMessage deletePerformance(Long id);

    PerformanceResponse getAllPerformances();

    void savePerformances(List<Performance> performanceEntries);

    void deleteByStudentIdAndTopicId(Long studentId, Long topicId);

    StudentPerformanceResponse getPerformancesBySchoolAndProject(Long schoolId, Long projectId);

    ResponseMessage updatePerformances(Long schoolId, Long projectId, StudentPerformanceResponse updatedPerformanceRequest);

    Pair<HttpHeaders, InputStreamResource> getStudentPerformanceUploadTemplate();
}
