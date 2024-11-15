package in.gurujifoundation.mapper;

import in.gurujifoundation.domain.*;
import in.gurujifoundation.request.CreateOrUpdateProjectCoordinatorRequest;
import in.gurujifoundation.response.ProjectCoordinatorDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SchoolProjectMappingMapper {

    SchoolProjectMappingMapper INSTANCE = Mappers.getMapper(SchoolProjectMappingMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", source = "project")
    @Mapping(target = "school", source = "school")
    @Mapping(target = "teacher", source = "teacher")
    @Mapping(target = "students", source = "students")
    SchoolProjectMapping mapToEntity(Project project, School school, Teacher teacher, List<Student> students);
}
