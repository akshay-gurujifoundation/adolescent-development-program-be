package in.gurujifoundation.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TopicPerformanceResponse {
    private Long topicId;
    private String topicName;
    private Float beforeInterventionMark;
    private Float afterInterventionMark;
}
