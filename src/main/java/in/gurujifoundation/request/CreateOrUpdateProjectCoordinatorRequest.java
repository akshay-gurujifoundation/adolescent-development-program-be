package in.gurujifoundation.request;

import lombok.Data;

@Data
public class CreateOrUpdateProjectCoordinatorRequest {

    private String name;

    private String areaOfExpertise;

    private String availability;

    private String mobileNumber;

    private String address;
}
