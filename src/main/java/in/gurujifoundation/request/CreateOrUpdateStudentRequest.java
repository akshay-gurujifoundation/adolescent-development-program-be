package in.gurujifoundation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateStudentRequest {

    private Long schoolId;

    private String name;

    private LocalDate dob;

    private CreateOrUpdateParentRequest parent;

    private String address;

    private String phoneNumber;

    private String alternativeNumber;

    private String email;
}
