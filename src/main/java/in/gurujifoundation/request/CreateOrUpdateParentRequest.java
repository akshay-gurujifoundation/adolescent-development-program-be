package in.gurujifoundation.request;

import lombok.Data;

@Data
public class CreateOrUpdateParentRequest {
    private Long id;
    private String name;

    private String occupation;

    private String phoneNumber;
}
