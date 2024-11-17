package in.gurujifoundation.response;

import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class SchoolProjectMappingDetails {
    private ProjectDetails project;
    private SchoolDetails school;
    private TeacherDetails teacher;
    @Builder.Default
    private Set<StudentDetails> students = new HashSet<>();
}
