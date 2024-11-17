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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public TopicResponse getAllTopics(Long projectId) {
        try {
            log.debug("Started fetching all projects");
            List<Topic> topics;
            if (projectId != null) {
                topics = topicRepository.findAllByProjectId(projectId);
            } else {
                topics = topicRepository.findAll();
            }
            log.debug("Started fetching all topics");
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

    @Override
    public ResponseMessage deleteTopics(Set<Topic> topics) {
        topicRepository.deleteAll(topics);
        return null;
    }

    public Topic getTopic(Long id) {
        Optional<Topic> topicOptional = topicRepository.findById(id);
        if (topicOptional.isEmpty()) {
            throw new EntityNotFoundException("Topic with id " + id + " not found");
        }
        return topicOptional.get();
    }

    @Override
    public void saveTopics(Set<Topic> topics) {
        topicRepository.saveAll(topics);
    }

    @Override
    public Set<Topic> createOrUpdateOrDeleteTopics(Project project, Set<CreateOrUpdateTopicRequest> topicRequests) {
        // Map requests by topic ID (assuming CreateOrUpdateTopicRequest has an `id` field for existing topics)
        Map<Long, CreateOrUpdateTopicRequest> topicRequestMap = topicRequests.stream()
                .filter(request -> request.getId() != null)
                .collect(Collectors.toMap(CreateOrUpdateTopicRequest::getId, Function.identity()));

        // Map existing topics by topic ID
        Map<Long, Topic> existingTopicsMap = project.getTopics().stream()
                .filter(topic -> topic.getId() != null)
                .collect(Collectors.toMap(Topic::getId, Function.identity()));

        Set<Topic> updatedTopics = new HashSet<>();

        // Handle updates and creations
        for (Map.Entry<Long, CreateOrUpdateTopicRequest> entry : topicRequestMap.entrySet()) {
            Long topicId = entry.getKey();
            CreateOrUpdateTopicRequest request = entry.getValue();
            Topic topic = existingTopicsMap.get(topicId);

            if (topic != null) {
                // Update existing topic
                topic.setName(request.getName());
                topic.setDescription(request.getDescription());
                existingTopicsMap.remove(topicId);
            } else {
                // Create new topic
                topic = Topic.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .project(project)
                        .build();
            }
            updatedTopics.add(topic);
        }

        for (CreateOrUpdateTopicRequest request : topicRequests) {
            if (request.getId() == null) {
                Topic newTopic = Topic.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .project(project)
                        .build();
                updatedTopics.add(newTopic);
            }
        }


        // Handle deletions
        if (!existingTopicsMap.isEmpty()) {
            topicRepository.deleteAll(existingTopicsMap.values());
        }

        return updatedTopics;
    }

}
