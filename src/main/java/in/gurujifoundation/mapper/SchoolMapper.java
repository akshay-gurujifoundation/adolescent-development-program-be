package in.gurujifoundation.mapper;

import in.gurujifoundation.domain.School;
import in.gurujifoundation.request.CreateSchoolRequest;
import in.gurujifoundation.response.SchoolDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SchoolMapper {

    SchoolMapper INSTANCE = Mappers.getMapper(SchoolMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "deletedAt", ignore = true)
    School toEntity(CreateSchoolRequest request);

    @Mapping(target = "id", source = "school.id")
    @Mapping(target = "name", source = "school.name")
    @Mapping(target = "address", source = "school.address")
    @Mapping(target = "phoneNumber", source = "school.phoneNumber")
    @Mapping(target = "principalName", source = "school.principalName")
    @Mapping(target = "principalContactNo", source = "school.principalContactNo")
    @Mapping(target = "managingTrustee", source = "school.managingTrustee")
    @Mapping(target = "trusteeContactInfo", source = "school.trusteeContactInfo")
    @Mapping(target = "website", source = "school.website")
    SchoolDetails toResponse(School school);

    List<SchoolDetails> toSchoolDetails(List<School> schools);
}
