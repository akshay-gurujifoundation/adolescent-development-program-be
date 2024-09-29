package in.gurujifoundation.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
public class TeacherResponse {

    @Schema(description = "Teacher Details")
    @NotNull
    @NotEmpty
    @Builder.Default
    private List<TeacherDetails> teacherDetails = new ArrayList<>();

}
