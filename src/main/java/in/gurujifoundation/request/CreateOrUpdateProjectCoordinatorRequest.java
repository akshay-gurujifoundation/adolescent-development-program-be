package in.gurujifoundation.request;

import in.gurujifoundation.dto.CreateUserRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateOrUpdateProjectCoordinatorRequest extends CreateUserRequest {

    @NotBlank(message = "Name Of Coordinator is required")
    private String name;

    private String areaOfExpertise;

    private String availability;

    @NotBlank(message = "Mobile Number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile Number should be 10 digits")
    private String mobileNumber;

    private String address;
}
