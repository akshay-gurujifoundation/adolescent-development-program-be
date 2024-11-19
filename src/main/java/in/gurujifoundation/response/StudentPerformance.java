package in.gurujifoundation.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class StudentPerformance {
    private Long studentId;
    private String studentName;
    private List<TopicPerformanceResponse> topics;
}
