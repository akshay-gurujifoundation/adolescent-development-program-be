package in.gurujifoundation.mapper;

import in.gurujifoundation.domain.Project;
import in.gurujifoundation.domain.School;
import in.gurujifoundation.request.CreateOrUpdateProjectRequest;
import in.gurujifoundation.response.ProjectDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {SchoolMapper.class})
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mapping(target = "name", source = "createOrUpdateProjectRequest.name")
    @Mapping(target = "description", source = "createOrUpdateProjectRequest.description")
    @Mapping(target = "startDate", source = "createOrUpdateProjectRequest.startDate")
    @Mapping(target = "endDate", source = "createOrUpdateProjectRequest.endDate")
    @Mapping(target = "actualStartDate", source = "createOrUpdateProjectRequest.actualStartDate")
    @Mapping(target = "actualEndDate", source = "createOrUpdateProjectRequest.actualEndDate")
    @Mapping(target = "status", source = "createOrUpdateProjectRequest.status")
    @Mapping(target = "school", source = "school")
    @Mapping(target = "id", ignore = true)
    Project mapToEntity(CreateOrUpdateProjectRequest createOrUpdateProjectRequest, School school);

    @Mapping(target = "description", source = "project.description")
    @Mapping(target = "startDate", source = "project.startDate")
    @Mapping(target = "endDate", source = "project.endDate")
    @Mapping(target = "actualStartDate", source = "project.actualStartDate")
    @Mapping(target = "actualEndDate", source = "project.actualEndDate")
    @Mapping(target = "status", source = "project.status")
    @Mapping(target = "schoolDetails", source = "project.school")
    ProjectDetails mapToProjectDetailsResponse(Project project);

    List<ProjectDetails> mapToProjectDetailsList(List<Project> projects);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "createOrUpdateProjectRequest.name")
    @Mapping(target = "description", source = "createOrUpdateProjectRequest.description")
    @Mapping(target = "startDate", source = "createOrUpdateProjectRequest.startDate")
    @Mapping(target = "endDate", source = "createOrUpdateProjectRequest.endDate")
    @Mapping(target = "actualStartDate", source = "createOrUpdateProjectRequest.actualStartDate")
    @Mapping(target = "actualEndDate", source = "createOrUpdateProjectRequest.actualEndDate")
    @Mapping(target = "status", source = "createOrUpdateProjectRequest.status")
    @Mapping(target = "school", source = "school")
    void updateProject(@MappingTarget Project project, CreateOrUpdateProjectRequest createOrUpdateProjectRequest, School school);
}
