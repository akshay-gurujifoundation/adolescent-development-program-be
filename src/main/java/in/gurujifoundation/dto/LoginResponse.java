package in.gurujifoundation.dto;

import in.gurujifoundation.domain.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private Long expiresIn;
    private UserRole role;
}