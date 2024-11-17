package in.gurujifoundation.request;

import lombok.Data;

@Data
public class CreateOrUpdateTopicRequest {
    private Long id;
    private String name;
    private String description;
    private Long projectId;
}
