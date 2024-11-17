package in.gurujifoundation.mapper;

import in.gurujifoundation.domain.Project;
import in.gurujifoundation.domain.ProjectCoordinator;
import in.gurujifoundation.request.CreateOrUpdateProjectRequest;
import in.gurujifoundation.response.ProjectDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(uses = {SchoolMapper.class})
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mapping(target = "name", source = "createOrUpdateProjectRequest.name")
    @Mapping(target = "description", source = "createOrUpdateProjectRequest.description")
    @Mapping(target = "status", source = "createOrUpdateProjectRequest.status")
    @Mapping(target = "projectCoordinators", source = "projectCoordinators")
    @Mapping(target = "id", ignore = true)
    Project mapToEntity(CreateOrUpdateProjectRequest createOrUpdateProjectRequest, Set<ProjectCoordinator> projectCoordinators);

    @Mapping(target = "description", source = "project.description")
    @Mapping(target = "status", source = "project.status")
    @Mapping(target = "topics", source = "project.topics")
    @Mapping(target = "projectCoordinators", source = "project.projectCoordinators")
    ProjectDetails mapToProjectDetailsResponse(Project project);

    List<ProjectDetails> mapToProjectDetailsList(List<Project> projects);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "createOrUpdateProjectRequest.name")
    @Mapping(target = "description", source = "createOrUpdateProjectRequest.description")
    @Mapping(target = "status", source = "createOrUpdateProjectRequest.status")
    void updateProject(@MappingTarget Project project, CreateOrUpdateProjectRequest createOrUpdateProjectRequest);
}
