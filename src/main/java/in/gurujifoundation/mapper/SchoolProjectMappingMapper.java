package in.gurujifoundation.mapper;

import in.gurujifoundation.domain.*;
import in.gurujifoundation.response.SchoolProjectMappingDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    @Mapping(target = "project", source = "schoolProjectMapping.project")
    @Mapping(target = "school", source = "schoolProjectMapping.school")
    @Mapping(target = "teacher", source = "schoolProjectMapping.teacher")
    @Mapping(target = "students", source = "schoolProjectMapping.students")
    SchoolProjectMappingDetails mapToSchoolProjectMappingDetails(SchoolProjectMapping schoolProjectMapping);

    List<SchoolProjectMappingDetails> mapToSchoolProjectMappingDerailsList(List<SchoolProjectMapping> schoolProjectMappings);
}
