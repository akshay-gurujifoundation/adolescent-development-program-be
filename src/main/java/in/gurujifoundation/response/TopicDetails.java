package in.gurujifoundation.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopicDetails {
    private Long id;
    private String name;
    private String description;
    private Long projectId;
    private String projectName;
}
