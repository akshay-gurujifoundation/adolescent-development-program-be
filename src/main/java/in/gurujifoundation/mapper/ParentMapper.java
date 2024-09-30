package in.gurujifoundation.mapper;

import in.gurujifoundation.domain.Parent;
import in.gurujifoundation.request.CreateOrUpdateParentRequest;
import in.gurujifoundation.response.ParentDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParentMapper {

    ParentMapper INSTANCE = Mappers.getMapper(ParentMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "occupation", source = "occupation")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    Parent toParent(CreateOrUpdateParentRequest parentRequest);

    @Mapping(target = "id", source = "parent.id")
    @Mapping(target = "name", source = "parent.name")
    @Mapping(target = "occupation", source = "parent.occupation")
    @Mapping(target = "phoneNumber", source = "parent.phoneNumber")
    ParentDetails toParentDetails(Parent parent);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "parentDetails.name")
    @Mapping(target = "occupation", source = "parentDetails.occupation")
    @Mapping(target = "phoneNumber", source = "parentDetails.phoneNumber")
    void updateParent(ParentDetails parentDetails, @MappingTarget Parent parent);
}
