package in.gurujifoundation.request;

import lombok.Data;

import java.time.LocalDate;

@Data
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
