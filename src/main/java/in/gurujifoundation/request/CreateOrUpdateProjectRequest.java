package in.gurujifoundation.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
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
    List<CreateOrUpdateTopicRequest> createOrUpdateTopicRequests = new ArrayList<>();
}
