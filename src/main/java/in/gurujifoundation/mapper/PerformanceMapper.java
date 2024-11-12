package in.gurujifoundation.mapper;

import in.gurujifoundation.domain.Performance;
import in.gurujifoundation.domain.Project;
import in.gurujifoundation.domain.Student;
import in.gurujifoundation.domain.Topic;
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
    @Mapping(target = "topic", source = "topic")
    @Mapping(target = "beforeInterventionMark", source = "createOrUpdatePerformanceRequest.beforeInterventionMark")
    @Mapping(target = "afterInterventionMark", source = "createOrUpdatePerformanceRequest.afterInterventionMark")
    Performance mapToEntity(CreateOrUpdatePerformanceRequest createOrUpdatePerformanceRequest, Student student, Topic topic);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "studentId", source = "performance.student.id")
    @Mapping(target = "topicId", source = "performance.topic.id")
    @Mapping(target = "beforeInterventionMark", source = "performance.beforeInterventionMark")
    @Mapping(target = "afterInterventionMark", source = "performance.afterInterventionMark")
    PerformanceDetails mapToPerformanceDetailsResponse(Performance performance);

    List<PerformanceDetails> mapToPerformanceDetailsList(List<Performance> performances);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updatePerformance(@MappingTarget Performance performance, CreateOrUpdatePerformanceRequest updatePerformanceRequest, Student student, Topic topic);
}
