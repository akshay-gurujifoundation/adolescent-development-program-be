package in.gurujifoundation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateParentRequest {
    private Long id;
    private String name;
    private String occupation;
    private String phoneNumber;
}
