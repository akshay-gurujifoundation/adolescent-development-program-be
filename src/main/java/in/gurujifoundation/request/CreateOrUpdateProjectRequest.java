package in.gurujifoundation.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateOrUpdateProjectRequest {
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private String status;
    private Long schoolId;
}
