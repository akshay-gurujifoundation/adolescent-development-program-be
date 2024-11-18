package in.gurujifoundation.request;

import in.gurujifoundation.constants.ErrorCodeConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ProjectSchoolMappingUpdateRequest {

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate actualStartDate;

    private LocalDate actualEndDate;

    @Schema(description = "Teacher to be assigned to project", example = "1")
    @NotEmpty(message = ErrorCodeConstant.TEACHER_ID_CAN_NOT_BE_NULL_OR_BLANK)
    private Long teacherId;

}
