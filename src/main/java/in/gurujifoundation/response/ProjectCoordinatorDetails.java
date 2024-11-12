package in.gurujifoundation.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectCoordinatorDetails {
    private Long id;
    private String name;

    private String areaOfExpertise;

    private String availability;

    private String mobileNumber;

    private String address;
}
