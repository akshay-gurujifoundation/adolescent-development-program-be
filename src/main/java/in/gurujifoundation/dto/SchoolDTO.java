package in.gurujifoundation.dto;

import lombok.Data;

@Data
public class SchoolDTO {
    private String name;
    private String address;
    private String phoneNumber;
    private String principalName;
    private String principalContactNo;
    private String managingTrustee;
    private String trusteeContactInfo;
    private String website;
    private Long userId;
}
