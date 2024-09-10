package in.gurujifoundation.service;

import in.gurujifoundation.request.CreateSchoolRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.SchoolDetails;
import in.gurujifoundation.response.SchoolsResponse;

public interface SchoolService {
    ResponseMessage createSchool(CreateSchoolRequest request);

    SchoolDetails getSchoolById(Long id);

    SchoolsResponse getSchools();
}
