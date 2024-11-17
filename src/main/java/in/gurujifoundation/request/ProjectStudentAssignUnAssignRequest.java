package in.gurujifoundation.request;

import in.gurujifoundation.constants.ErrorCodeConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ProjectStudentAssignUnAssignRequest {

    @Schema(description = "School to be assigned to project", example = "1")
    @NotEmpty(message = ErrorCodeConstant.SCHOOL_ID_CAN_NOT_BE_NULL_OR_BLANK)
    @NotNull(message = ErrorCodeConstant.SCHOOL_ID_CAN_NOT_BE_NULL_OR_BLANK)
    private Long schoolId;

    @Schema(description = "Students to be assigned to project", example = "[1,2]")
    @NotEmpty(message = ErrorCodeConstant.STUDENT_IDS_CANNOT_BE_NULL_OR_EMPTY)
    private Set<Long> studentIds;
}
