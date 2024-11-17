package in.gurujifoundation.request;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class CreateOrUpdateProjectRequest {
    private String name;
    private String description;
    private String status;
    private List<Long> projectCoordinatorIds;
    @Builder.Default
    List<CreateOrUpdateTopicRequest> createOrUpdateTopicRequests = new ArrayList<>();
}
