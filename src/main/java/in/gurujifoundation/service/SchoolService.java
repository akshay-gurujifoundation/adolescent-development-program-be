package in.gurujifoundation.service;

import in.gurujifoundation.domain.School;
import in.gurujifoundation.dto.BulkUploadResponse;
import in.gurujifoundation.request.CreateOrUpdateSchoolRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.SchoolDetails;
import in.gurujifoundation.response.SchoolsResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SchoolService {
    ResponseMessage createSchool(CreateOrUpdateSchoolRequest createSchoolRequest);

    SchoolDetails getSchoolById(Long id);

    SchoolsResponse getSchools();

    ResponseMessage updateSchool(CreateOrUpdateSchoolRequest updateSchoolRequest, Long id);

    School getSchool(Long schoolId);

    ResponseMessage deleteSchool(Long id);

    SchoolsResponse getUnAssignSchoolsForProject(Long projectId);

    List<School> getAllSchools();

    Pair<HttpHeaders, InputStreamResource> getSchoolUploadTemplate();

    BulkUploadResponse uploadSchoolExcel(MultipartFile file);
}
