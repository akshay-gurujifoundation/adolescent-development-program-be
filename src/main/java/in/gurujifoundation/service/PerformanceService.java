package in.gurujifoundation.service;

import in.gurujifoundation.request.CreateOrUpdatePerformanceRequest;
import in.gurujifoundation.request.CreateOrUpdateProjectRequest;
import in.gurujifoundation.response.*;
import jakarta.validation.Valid;

public interface PerformanceService {
    ResponseMessage createPerformance(@Valid CreateOrUpdatePerformanceRequest createOrUpdatePerformanceRequest);

    ResponseMessage updatePerformance(@Valid CreateOrUpdatePerformanceRequest updatePerformanceRequest, Long id);

    PerformanceDetails getPerformanceById(Long id);

    ResponseMessage deletePerformance(Long id);

    PerformanceResponse getAllPerformances();

}
