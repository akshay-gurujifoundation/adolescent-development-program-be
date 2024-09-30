package in.gurujifoundation.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class StudentDetails {
    private Long id;
    private String name;
    private LocalDate dob;
    private String address;
    private String phoneNumber;
    private String alternativeNumber;
    private String email;
    private ParentDetails parent;
}
