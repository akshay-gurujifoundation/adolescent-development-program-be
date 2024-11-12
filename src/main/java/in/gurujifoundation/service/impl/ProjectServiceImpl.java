package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.Project;
import in.gurujifoundation.domain.Student;
import in.gurujifoundation.exception.EntityNotFoundException;
import in.gurujifoundation.exception.InternalServerException;
import in.gurujifoundation.mapper.ProjectMapper;
import in.gurujifoundation.repository.ProjectRepository;
import in.gurujifoundation.request.CreateOrUpdateProjectRequest;
import in.gurujifoundation.request.ProjectStudentAllocationDeAllocationRequest;
import in.gurujifoundation.response.*;
import in.gurujifoundation.service.ProjectService;
import in.gurujifoundation.service.SchoolService;
import in.gurujifoundation.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final StudentService studentService;

    public ProjectServiceImpl(SchoolService schoolService, ProjectRepository projectRepository, StudentService studentService) {
        this.projectRepository = projectRepository;
        this.studentService = studentService;
    }

    @Override
    public ResponseMessage createProject(CreateOrUpdateProjectRequest createOrUpdateProjectRequest) {
        try {
            log.debug("Started saving project with name: {}", createOrUpdateProjectRequest.getName());
            Project project = ProjectMapper.INSTANCE.mapToEntity(createOrUpdateProjectRequest);
            projectRepository.save(project);
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
                projects = projectRepository.findAllBySchools_Id(schoolId);
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
    public ResponseMessage allocateProjectToStudents(Long id, ProjectStudentAllocationDeAllocationRequest projectStudentAllocationDeAllocationRequest) {
        Project project = getProject(id);
        List<Student> students = studentService.getStudentsByIds(projectStudentAllocationDeAllocationRequest.getStudentIds());
        for (Student student : students) {
            Set<Project> projects = student.getProjects();
            projects.add(project);
            student.setProjects(projects);
            project.getStudents().add(student);
        }
        studentService.updateStudents(students);
        projectRepository.save(project);
        return ResponseMessage.builder().message(ErrorCodeConstant.STUDENTS_ALLOCATED_TO_PROJECT_SUCCESSFULLY).build();
    }

    @Override
    public ResponseMessage deallocateProjectToStudents(Long id, ProjectStudentAllocationDeAllocationRequest projectStudentAllocationDeAllocationRequest) {
        Project project = getProject(id);
        List<Student> students = studentService.getStudentsByIds(projectStudentAllocationDeAllocationRequest.getStudentIds());
        for (Student student : students) {
            Set<Project> projects = student.getProjects();
            projects.remove(project);
            student.setProjects(projects);
            project.getStudents().remove(student);
        }
        studentService.updateStudents(students);
        projectRepository.save(project);
        return ResponseMessage.builder().message(ErrorCodeConstant.STUDENTS_DE_ALLOCATED_TO_PROJECT_SUCCESSFULLY).build();
    }

    @Override
    public void saveProject(Project project) {
        projectRepository.save(project);
    }

}
