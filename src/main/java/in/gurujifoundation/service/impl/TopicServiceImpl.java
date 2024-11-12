package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.Project;
import in.gurujifoundation.domain.Topic;
import in.gurujifoundation.exception.EntityNotFoundException;
import in.gurujifoundation.exception.InternalServerException;
import in.gurujifoundation.mapper.TopicMapper;
import in.gurujifoundation.repository.TopicRepository;
import in.gurujifoundation.request.CreateOrUpdateTopicRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.TopicDetails;
import in.gurujifoundation.response.TopicResponse;
import in.gurujifoundation.service.ProjectService;
import in.gurujifoundation.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    private final ProjectService projectService;

    public TopicServiceImpl(TopicRepository topicRepository, ProjectService projectService) {
        this.topicRepository = topicRepository;
        this.projectService = projectService;
    }

    @Override
    public ResponseMessage createTopic(CreateOrUpdateTopicRequest createOrUpdateTopicRequest) {
        Project project = projectService.getProject(createOrUpdateTopicRequest.getProjectId());
        Topic topic = TopicMapper.INSTANCE.mapToEntity(createOrUpdateTopicRequest, project);
        topicRepository.save(topic);
        project.getTopics().add(topic);
        projectService.saveProject(project);
        return ResponseMessage.builder().message(ErrorCodeConstant.TOPIC_CREATED_SUCCESSFULLY).build();
    }

    @Override
    public ResponseMessage updateTopic(CreateOrUpdateTopicRequest createOrUpdateTopicRequest, Long id) {
        Topic topic = getTopic(id);
        Project project = projectService.getProject(createOrUpdateTopicRequest.getProjectId());
        TopicMapper.INSTANCE.updateEntity(topic, createOrUpdateTopicRequest, project);
        topicRepository.save(topic);
        project.getTopics().add(topic);
        projectService.saveProject(project);
        return ResponseMessage.builder().message(ErrorCodeConstant.TOPIC_UPDATED_SUCCESSFULLY).build();
    }

    @Override
    public TopicDetails getTopicById(Long id) {
        try {
            log.debug("Started fetching topic with id: {}", id);
            Topic topic = getTopic(id);
            log.debug("Successfully retrieved topic with id: {}", id);
            return TopicMapper.INSTANCE.mapToTopicDetailsResponse(topic);
        } catch (Exception e) {
            log.error("Error occurred while fetching topic with id: {} ", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public TopicResponse getAllTopics() {
        try {
            log.debug("Started fetching all topics");
            List<Topic> topics = topicRepository.findAll();
            List<TopicDetails> topicDetails = TopicMapper.INSTANCE.mapToTopicDetailsList(topics);
            log.debug("Successfully fetched all topics");
            return TopicResponse.builder().topics(topicDetails).build();
        } catch (Exception e) {
            log.error("Error occurred while fetching all topics", e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public ResponseMessage deleteTopic(Long id) {
        try {
            log.debug("Started deleting topic with id: {}", id);
            topicRepository.deleteById(id);
            log.debug("Successfully deleted topic with id: {}", id);
            return ResponseMessage.builder().message(ErrorCodeConstant.TOPIC_DELETED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while deleting topic with id: {}", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    public Topic getTopic(Long id) {
        Optional<Topic> topicOptional = topicRepository.findById(id);
        if (topicOptional.isEmpty()) {
            throw new EntityNotFoundException("Topic with id " + id + " not found");
        }
        return topicOptional.get();
    }
}
