package in.gurujifoundation.request;

import in.gurujifoundation.dto.CreateUserRequest;
import lombok.Data;

@Data
public class CreateOrUpdateProjectCoordinatorRequest extends CreateUserRequest {

    private String name;

    private String areaOfExpertise;

    private String availability;

    private String mobileNumber;

    private String address;
}
