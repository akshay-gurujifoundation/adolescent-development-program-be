package in.gurujifoundation.mapper;

import in.gurujifoundation.domain.School;
import in.gurujifoundation.request.CreateOrUpdateSchoolRequest;
import in.gurujifoundation.response.SchoolDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SchoolMapper {

    SchoolMapper INSTANCE = Mappers.getMapper(SchoolMapper.class);

    @Mapping(target = "id", ignore = true)
    School mapToEntity(CreateOrUpdateSchoolRequest request);

    @Mapping(target = "id", source = "school.id")
    @Mapping(target = "name", source = "school.name")
    @Mapping(target = "address", source = "school.address")
    @Mapping(target = "phoneNumber", source = "school.phoneNumber")
    @Mapping(target = "principalName", source = "school.principalName")
    @Mapping(target = "principalContactNo", source = "school.principalContactNo")
    @Mapping(target = "managingTrustee", source = "school.managingTrustee")
    @Mapping(target = "trusteeContactInfo", source = "school.trusteeContactInfo")
    @Mapping(target = "website", source = "school.website")
    SchoolDetails mapToSchoolDetails(School school);

    List<SchoolDetails> mapToSchoolDetailsList(List<School> schools);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "createOrUpdateSchoolRequest.name")
    @Mapping(target = "address", source = "createOrUpdateSchoolRequest.address")
    @Mapping(target = "phoneNumber", source = "createOrUpdateSchoolRequest.phoneNumber")
    @Mapping(target = "principalName", source = "createOrUpdateSchoolRequest.principalName")
    @Mapping(target = "principalContactNo", source = "createOrUpdateSchoolRequest.principalContactNo")
    @Mapping(target = "managingTrustee", source = "createOrUpdateSchoolRequest.managingTrustee")
    @Mapping(target = "trusteeContactInfo", source = "createOrUpdateSchoolRequest.trusteeContactInfo")
    @Mapping(target = "website", source = "createOrUpdateSchoolRequest.website")
    void updateSchool(CreateOrUpdateSchoolRequest createOrUpdateSchoolRequest, @MappingTarget School school);
}
