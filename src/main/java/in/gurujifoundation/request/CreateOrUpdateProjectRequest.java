package in.gurujifoundation.request;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
public class CreateOrUpdateProjectRequest {
    private String name;
    private String description;
    private String status;
    private List<Long> projectCoordinatorIds;
    @Builder.Default
    Set<CreateOrUpdateTopicRequest> createOrUpdateTopicRequests = new HashSet<>();
}
