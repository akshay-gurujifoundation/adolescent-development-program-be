package in.gurujifoundation.mapper;

import in.gurujifoundation.domain.Performance;
import in.gurujifoundation.domain.Project;
import in.gurujifoundation.domain.Student;
import in.gurujifoundation.request.CreateOrUpdatePerformanceRequest;
import in.gurujifoundation.response.PerformanceDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PerformanceMapper {

    PerformanceMapper INSTANCE = Mappers.getMapper(PerformanceMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", source = "student")
    @Mapping(target = "project", source = "project")
    @Mapping(target = "attendanceGrade", source = "createOrUpdatePerformanceRequest.attendanceGrade")
    Performance mapToEntity(CreateOrUpdatePerformanceRequest createOrUpdatePerformanceRequest, Student student, Project project);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "studentId", source = "performance.student.id")
    @Mapping(target = "projectId", source = "performance.project.id")
    PerformanceDetails mapToPerformanceDetailsResponse(Performance performance);

    List<PerformanceDetails> mapToPerformanceDetailsList(List<Performance> performances);

    @Mapping(target = "id", ignore = true)
    void updatePerformance(@MappingTarget Performance performance, CreateOrUpdatePerformanceRequest updatePerformanceRequest, Student student, Project project);
}
