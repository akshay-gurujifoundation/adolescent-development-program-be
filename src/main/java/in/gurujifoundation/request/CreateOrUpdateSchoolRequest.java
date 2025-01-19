package in.gurujifoundation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
