package in.gurujifoundation.mapper;

import in.gurujifoundation.domain.Project;
import in.gurujifoundation.domain.Topic;
import in.gurujifoundation.request.CreateOrUpdateTopicRequest;
import in.gurujifoundation.response.TopicDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TopicMapper {

    TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "createOrUpdateTopicRequest.name")
    @Mapping(target = "description", source = "createOrUpdateTopicRequest.description")
    @Mapping(target = "project", source = "project")
    Topic mapToEntity(CreateOrUpdateTopicRequest createOrUpdateTopicRequest, Project project);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "topic.name")
    @Mapping(target = "description", source = "topic.description")
    @Mapping(target = "projectId", source = "topic.project.id")
    @Mapping(target = "projectName", source = "topic.project.name")
    TopicDetails mapToTopicDetailsResponse(Topic topic);

    List<TopicDetails> mapToTopicDetailsList(List<Topic> topics);

    @Mapping(target = "id", ignore = true)
    @Mapping(target ="name",  source = "createOrUpdateTopicRequest.name")
    @Mapping(target ="description",  source = "createOrUpdateTopicRequest.description")
    @Mapping(target ="project",  source = "project")
    void updateEntity(@MappingTarget Topic topic, CreateOrUpdateTopicRequest createOrUpdateTopicRequest, Project project);
}
