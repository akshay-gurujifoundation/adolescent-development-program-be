package in.gurujifoundation.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherDetails {

    private Long id;
    private String name;
    private Integer experience;
    private SchoolDetails schoolDetails;
}
