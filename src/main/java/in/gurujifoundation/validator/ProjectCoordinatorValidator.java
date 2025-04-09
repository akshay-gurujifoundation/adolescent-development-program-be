package in.gurujifoundation.validator;

import in.gurujifoundation.request.CreateOrUpdateProjectCoordinatorRequest;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProjectCoordinatorValidator {
    
    public void validateProjectCoordinatorRequest(CreateOrUpdateProjectCoordinatorRequest request) {
        List<String> validationErrors = new ArrayList<>();

        if (StringUtils.isBlank(request.getPassword())) {
            validationErrors.add("Password is required");
        }

        if (StringUtils.isBlank(request.getEmail())) {
            validationErrors.add("Email is required");
        } else if (!EmailValidator.getInstance().isValid(request.getEmail())) {
            validationErrors.add("Invalid email format");
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Invalid project coordinator data: " + String.join(", ", validationErrors));
        }
    }
}