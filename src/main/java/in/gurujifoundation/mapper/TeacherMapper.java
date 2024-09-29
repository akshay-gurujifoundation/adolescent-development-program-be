package in.gurujifoundation.mapper;

import in.gurujifoundation.domain.School;
import in.gurujifoundation.domain.Teacher;
import in.gurujifoundation.request.CreateOrUpdateTeacherRequest;
import in.gurujifoundation.response.TeacherDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {SchoolMapper.class})
public interface TeacherMapper {

    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    @Mapping(target = "name", source = "createTeacherRequest.name")
    @Mapping(target = "experience", source = "createTeacherRequest.experience")
    @Mapping(target = "school", source = "school")
    Teacher mapToEntity(CreateOrUpdateTeacherRequest createTeacherRequest, School school);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "experience", source = "experience")
    @Mapping(target = "schoolDetails", source = "school")
    TeacherDetails mapToTeacherDetailsResponse(Teacher teacher);

    List<TeacherDetails> mapToTeacherDetailsList(List<Teacher> teachers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "updateTeacherRequest.name")
    @Mapping(target = "experience", source = "updateTeacherRequest.experience")
    @Mapping(target = "school", source = "school")
    void updateTeacher(@MappingTarget Teacher teacher, CreateOrUpdateTeacherRequest updateTeacherRequest, School school);
}
