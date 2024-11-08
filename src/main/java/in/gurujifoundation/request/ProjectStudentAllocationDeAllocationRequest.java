package in.gurujifoundation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ProjectStudentAllocationDeAllocationRequest {

    @Schema(description = "Id list for allocation of project", example = "[1,2]")
    @NotEmpty(message = in.gurujifoundation.constants.ErrorCodeConstant.ID_CANT_BE_BLANK)
    private List<Long> studentIds;
}
