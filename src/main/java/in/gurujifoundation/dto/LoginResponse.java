package in.gurujifoundation.dto;

import in.gurujifoundation.domain.UserRole;
import io.micrometer.core.instrument.distribution.StepBucketHistogram;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String email;
    private String name;
    private String token;
    private Long expiresIn;
    private UserRole role;
}