package in.gurujifoundation.service;

import in.gurujifoundation.request.CreateOrUpdateTeacherRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.TeacherDetails;
import in.gurujifoundation.response.TeacherResponse;

public interface TeacherService {
    ResponseMessage createTeacher(CreateOrUpdateTeacherRequest createTeacherRequest);

    TeacherDetails getTeacherById(Long id);

    TeacherResponse getAllTeachers();

    ResponseMessage updateTeacher(CreateOrUpdateTeacherRequest updateTeacherRequest, Long id);

    ResponseMessage deleteTeacher(Long id);
}
