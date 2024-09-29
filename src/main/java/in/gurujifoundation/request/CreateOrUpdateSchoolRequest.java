package in.gurujifoundation.request;

import lombok.Data;

@Data
public class CreateOrUpdateSchoolRequest {
    private String name;
    private String address;
    private String phoneNumber;
    private String principalName;
    private String principalContactNo;
    private String managingTrustee;
    private String trusteeContactInfo;
    private String website;
}
