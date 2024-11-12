package in.gurujifoundation.request;

import lombok.Data;

@Data
public class CreateOrUpdateTopicRequest {
    private String name;
    private String description;
    private Long projectId;
}
