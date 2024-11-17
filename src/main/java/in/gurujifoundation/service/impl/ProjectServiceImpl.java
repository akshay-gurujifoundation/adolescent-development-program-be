package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.*;
import in.gurujifoundation.exception.EntityNotFoundException;
import in.gurujifoundation.exception.InternalServerException;
import in.gurujifoundation.mapper.ProjectMapper;
import in.gurujifoundation.mapper.TopicMapper;
import in.gurujifoundation.repository.ProjectRepository;
import in.gurujifoundation.request.CreateOrUpdateProjectRequest;
import in.gurujifoundation.request.ProjectAssignRequest;
import in.gurujifoundation.response.*;
import in.gurujifoundation.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final StudentService studentService;

    private final SchoolService schoolService;

    private final TeacherService teacherService;

    private final SchoolProjectMappingService schoolProjectMappingService;

    private final ProjectCoordinatorService projectCoordinatorService;

    private final TopicService topicService;

    @Autowired
    @Lazy
    public ProjectServiceImpl(SchoolService schoolService, ProjectRepository projectRepository, StudentService studentService, SchoolService schoolService1, TeacherService teacherService, SchoolProjectMappingService schoolProjectMappingService, ProjectCoordinatorService projectCoordinatorService, TopicService topicService) {
        this.projectRepository = projectRepository;
        this.studentService = studentService;
        this.schoolService = schoolService;
        this.teacherService = teacherService;
        this.schoolProjectMappingService = schoolProjectMappingService;
        this.projectCoordinatorService = projectCoordinatorService;
        this.topicService = topicService;
    }

    @Override
    public ResponseMessage createProject(CreateOrUpdateProjectRequest createOrUpdateProjectRequest) {
        try {
            log.debug("Started saving project with name: {}", createOrUpdateProjectRequest.getName());

            Set<ProjectCoordinator> projectCoordinators = projectCoordinatorService.getProjectCoordinators(createOrUpdateProjectRequest.getProjectCoordinatorIds());
            Project project = ProjectMapper.INSTANCE.mapToEntity(createOrUpdateProjectRequest, projectCoordinators);

            projectCoordinators.forEach(coordinator -> coordinator.getProjects().add(project));
            projectCoordinatorService.saveProjectCoordinators(projectCoordinators);

            Set<Topic> topics = createOrUpdateProjectRequest.getCreateOrUpdateTopicRequests().stream()
                    .map(request -> TopicMapper.INSTANCE.mapToEntity(request, project))
                    .collect(Collectors.toSet());

            project.setTopics(topics);
            projectRepository.save(project);

            topicService.saveTopics(topics);

            log.debug("Successfully saved project with name: {}", createOrUpdateProjectRequest.getName());
            return ResponseMessage.builder().message(ErrorCodeConstant.PROJECT_CREATED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while saving project with name: {}", createOrUpdateProjectRequest.getName(), e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public ProjectDetails getProjectById(Long id) {
        try {
            log.debug("Started fetching project with id: {}", id);
            Project project = getProject(id);
            log.debug("Successfully retrieved project with id: {}", id);
            return ProjectMapper.INSTANCE.mapToProjectDetailsResponse(project);
        } catch (Exception e) {
            log.error("Error occurred while fetching project with id: {} ", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public ResponseMessage updateProject(CreateOrUpdateProjectRequest updateProjectRequest, Long id) {
        try {
            log.debug("Started updating project with id: {}", id);
            Project Project = getProject(id);
            ProjectMapper.INSTANCE.updateProject(Project, updateProjectRequest);
            projectRepository.save(Project);
            log.debug("Successfully updated project with id: {}", id);
            return ResponseMessage.builder().message(ErrorCodeConstant.PROJECT_UPDATED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while updating Project with id: {}", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public ProjectResponse getAllProjects(Long schoolId) {
        try {
            log.debug("Started fetching all projects");
            List<Project> projects;
            if (schoolId != null) {
                projects = projectRepository.findAllBySchoolId(schoolId);
            } else {
                projects = projectRepository.findAll();
            }
            List<ProjectDetails> ProjectDetails = ProjectMapper.INSTANCE.mapToProjectDetailsList(projects);
            log.debug("Successfully fetched all projects");
            return ProjectResponse.builder().projects(ProjectDetails).build();
        } catch (Exception e) {
            log.error("Error occurred while fetching all projects", e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public ResponseMessage deleteProject(Long id) {
        try {
            log.debug("Started deleting project with id: {}", id);
            projectRepository.deleteById(id);
            log.debug("Successfully deleted project with id: {}", id);
            return ResponseMessage.builder().message(ErrorCodeConstant.PROJECT_DELETED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while deleting project with id: {}", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public Project getProject(Long id) {
        log.debug("Starting to fetch project details for id: {}", id);
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (projectOptional.isEmpty()) {
            log.warn("No project found for id: {}", id);
            throw new EntityNotFoundException(ErrorCodeConstant.PROJECT_DOES_NOT_EXIST);
        }
        return projectOptional.get();
    }


    @Override
    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    @Override
    public ResponseMessage assignProjectToSchool(Long id, ProjectAssignRequest projectAssignRequest) {
        try {
            Project project = getProject(id);
            School school = schoolService.getSchool(projectAssignRequest.getSchoolId());
            Teacher teacher = teacherService.getTeacher(projectAssignRequest.getTeacherId());
            List<Student> studentList = studentService.getStudentsByIds(projectAssignRequest.getStudentIds());
            schoolProjectMappingService.saveSchoolProjectMapping(project, school, teacher, studentList);
            return ResponseMessage.builder().message(ErrorCodeConstant.SUCCESSFULLY_ASSIGNED_SCHOOL_TEACHER_AND_STUDENTS_TO_PROJECT).build();
        } catch (Exception e) {
            log.error("Error occurred while assigning school, teacher and students to project with id: {}", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public ProjectSchoolMappingResponse getProjectSchoolMapping(Long SchoolId) {
        try {
            List<SchoolProjectMappingDetails> schoolProjectMappingDetails = schoolProjectMappingService.getAllSchoolProjectMappings(SchoolId);
            return ProjectSchoolMappingResponse.builder().schoolProjects(schoolProjectMappingDetails).build();
        } catch (Exception e) {
            log.error("Error occurred while fetching associated project to school for id: {}", SchoolId, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

}
