package in.gurujifoundation.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class UploadStats {
    private int successCount = 0;
    private int failureCount = 0;
    private int totalProcessed = 0;
    private List<String> failedStudents = new ArrayList<>();

    public void incrementSuccessCount() {
        successCount++;
        totalProcessed++;
    }

    public void incrementFailureCount() {
        failureCount++;
        totalProcessed++;
    }
}