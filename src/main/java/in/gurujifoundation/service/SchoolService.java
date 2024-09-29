package in.gurujifoundation.service;

import in.gurujifoundation.request.CreateOrUpdateSchoolRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.SchoolDetails;
import in.gurujifoundation.response.SchoolsResponse;

public interface SchoolService {
    ResponseMessage createSchool(CreateOrUpdateSchoolRequest request);

    SchoolDetails getSchoolById(Long id);

    SchoolsResponse getSchools();

    ResponseMessage updateSchool(CreateOrUpdateSchoolRequest createOrUpdateSchoolRequest, Long id);
}
