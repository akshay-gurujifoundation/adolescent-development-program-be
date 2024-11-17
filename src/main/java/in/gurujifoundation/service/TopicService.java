package in.gurujifoundation.service;

import in.gurujifoundation.domain.Project;
import in.gurujifoundation.domain.Topic;
import in.gurujifoundation.request.CreateOrUpdateTopicRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.TopicDetails;
import in.gurujifoundation.response.TopicResponse;
import jakarta.validation.Valid;

import java.util.Set;

public interface TopicService {
    ResponseMessage createTopic(@Valid CreateOrUpdateTopicRequest createOrUpdateTopicRequest);

    ResponseMessage updateTopic(CreateOrUpdateTopicRequest createOrUpdateTopicRequest, Long id);

    TopicDetails getTopicById(Long id);

    TopicResponse getAllTopics(Long projectId);

    ResponseMessage deleteTopic(Long id);
    ResponseMessage deleteTopics(Set<Topic> ids);

    Topic getTopic(Long id);

    void saveTopics(Set<Topic> topics);

    Set<Topic> createOrUpdateOrDeleteTopics(Project project, Set<CreateOrUpdateTopicRequest> topicRequests);
}
