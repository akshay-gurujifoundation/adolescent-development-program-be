package in.gurujifoundation.service;

import in.gurujifoundation.domain.Topic;
import in.gurujifoundation.request.CreateOrUpdateTopicRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.TopicDetails;
import in.gurujifoundation.response.TopicResponse;
import jakarta.validation.Valid;

public interface TopicService {
    ResponseMessage createTopic(@Valid CreateOrUpdateTopicRequest createOrUpdateTopicRequest);

    ResponseMessage updateTopic(CreateOrUpdateTopicRequest createOrUpdateTopicRequest, Long id);

    TopicDetails getTopicById(Long id);

    TopicResponse getAllTopics();

    ResponseMessage deleteTopic(Long id);

    Topic getTopic(Long id);
}
