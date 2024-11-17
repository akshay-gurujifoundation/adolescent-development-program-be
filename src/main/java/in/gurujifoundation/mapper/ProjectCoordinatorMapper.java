package in.gurujifoundation.mapper;

import in.gurujifoundation.domain.ProjectCoordinator;
import in.gurujifoundation.request.CreateOrUpdateProjectCoordinatorRequest;
import in.gurujifoundation.response.ProjectCoordinatorDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProjectCoordinatorMapper {

    ProjectCoordinatorMapper INSTANCE = Mappers.getMapper(ProjectCoordinatorMapper.class);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "createOrUpdateProjectCoordinatorRequest.name")
    @Mapping(target = "areaOfExpertise", source = "createOrUpdateProjectCoordinatorRequest.areaOfExpertise")
    @Mapping(target = "availability", source = "createOrUpdateProjectCoordinatorRequest.availability")
    @Mapping(target = "mobileNumber", source = "createOrUpdateProjectCoordinatorRequest.mobileNumber")
    @Mapping(target = "address", source = "createOrUpdateProjectCoordinatorRequest.address")
    ProjectCoordinator mapToEntity(CreateOrUpdateProjectCoordinatorRequest createOrUpdateProjectCoordinatorRequest);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "areaOfExpertise", source = "areaOfExpertise")
    @Mapping(target = "availability", source = "availability")
    @Mapping(target = "mobileNumber", source = "mobileNumber")
    @Mapping(target = "address", source = "address")
    ProjectCoordinatorDetails mapToProjectCoordinatorDetailsResponse(ProjectCoordinator projectCoordinator);

    List<ProjectCoordinatorDetails> mapToProjectCoordinatorDetailsList(List<ProjectCoordinator> projectCoordinators);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "createOrUpdateProjectCoordinatorRequest.name")
    @Mapping(target = "areaOfExpertise", source = "createOrUpdateProjectCoordinatorRequest.areaOfExpertise")
    @Mapping(target = "availability", source = "createOrUpdateProjectCoordinatorRequest.availability")
    @Mapping(target = "mobileNumber", source = "createOrUpdateProjectCoordinatorRequest.mobileNumber")
    @Mapping(target = "address", source = "createOrUpdateProjectCoordinatorRequest.address")
    void updateEntity(@MappingTarget ProjectCoordinator projectCoordinator, CreateOrUpdateProjectCoordinatorRequest createOrUpdateProjectCoordinatorRequest);
}
