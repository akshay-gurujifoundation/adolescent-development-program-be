package in.gurujifoundation.service;

import in.gurujifoundation.domain.School;
import in.gurujifoundation.request.CreateOrUpdateSchoolRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.SchoolDetails;
import in.gurujifoundation.response.SchoolsResponse;

public interface SchoolService {
    ResponseMessage createSchool(CreateOrUpdateSchoolRequest createSchoolRequest);

    SchoolDetails getSchoolById(Long id);

    SchoolsResponse getSchools();

    ResponseMessage updateSchool(CreateOrUpdateSchoolRequest updateSchoolRequest, Long id);

    School getSchool(Long schoolId);

    ResponseMessage deleteSchool(Long id);
}
