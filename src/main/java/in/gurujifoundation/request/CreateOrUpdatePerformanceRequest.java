package in.gurujifoundation.request;

import lombok.Data;

@Data
public class CreateOrUpdatePerformanceRequest {
    private Long studentId;
    private Long topicId;
    private Float beforeInterventionMark;
    private Float afterInterventionMark;
}
