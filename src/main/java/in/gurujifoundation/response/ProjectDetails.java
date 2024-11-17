package in.gurujifoundation.response;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDetails {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private String status;
    private List<TopicDetails> topics = new ArrayList<>();
//    private List<SchoolProjectMappingDetails> schoolProjects = new ArrayList<>();
}
