package in.gurujifoundation.request;

import in.gurujifoundation.constants.ErrorCodeConstant;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class StudentPerformanceExcelDownloadRequest {

    @NotNull(message = ErrorCodeConstant.SCHOOL_ID_CANNOT_BE_NULL)
    private Long schoolId;

    @NotNull(message = ErrorCodeConstant.PROJECT_ID_CANNOT_BE_NULL)
    private Long projectId;

    @NotEmpty(message = ErrorCodeConstant.TOPIC_IDS_CANNOT_BE_NULL_OR_EMPTY)
    private List<Long> topicIds;
}
