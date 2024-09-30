package in.gurujifoundation.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParentDetails {
    private Long id;
    private String name;
    private String occupation;
    private String phoneNumber;
}
