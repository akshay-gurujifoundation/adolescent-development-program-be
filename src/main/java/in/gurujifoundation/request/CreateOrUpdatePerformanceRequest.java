package in.gurujifoundation.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateOrUpdatePerformanceRequest {
    private Long studentId;
    private Long projectId;
    private String attendanceGrade;
}
