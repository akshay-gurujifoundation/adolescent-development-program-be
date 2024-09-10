package in.gurujifoundation.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SchoolDetails {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String principalName;
    private String principalContactNo;
    private String managingTrustee;
    private String trusteeContactInfo;
    private String website;
}
