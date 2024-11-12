package in.gurujifoundation.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PerformanceDetails {
    private Long id;
    private Long studentId;
    private Long topicId;
    private Float beforeInterventionMark;
    private Float afterInterventionMark;
}
