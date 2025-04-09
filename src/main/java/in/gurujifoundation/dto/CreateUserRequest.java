package in.gurujifoundation.dto;

import in.gurujifoundation.domain.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    private String email;
    private String password;
    UserRole userRole;
}
