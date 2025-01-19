package in.gurujifoundation.validator;


import in.gurujifoundation.request.CreateOrUpdateParentRequest;
import in.gurujifoundation.request.CreateOrUpdateStudentRequest;
import in.gurujifoundation.response.ResponseMessage;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentValidator {

    /**
     * Validates the details of a student.
     *
     * @param createOrUpdateStudentRequest the request object containing student details
     * @param messages                     the list to store validation error messages
     * @param rowNum                       the row number for reference in error messages
     * @return true if the student details are valid, false otherwise
     */
    public static boolean validateStudent(CreateOrUpdateStudentRequest createOrUpdateStudentRequest,
                                          List<ResponseMessage> messages, int rowNum) {
        List<String> errors = new ArrayList<>();

        // Student validations
        if (StringUtils.isBlank(createOrUpdateStudentRequest.getName())) {
            errors.add("Student name is required");
        }

        // Date of birth validations
        if (createOrUpdateStudentRequest.getDob() == null) {
            errors.add("Date of birth is required");
        } else if (createOrUpdateStudentRequest.getDob().isEqual(LocalDate.now()) ||
                   createOrUpdateStudentRequest.getDob().isAfter(LocalDate.now())) {
            errors.add("Date of birth cannot be current or future date");
        }

        if (StringUtils.isBlank(createOrUpdateStudentRequest.getAddress())) {
            errors.add("Address is required");
        }

        // Optional phone number validation
        if (StringUtils.isNotBlank(createOrUpdateStudentRequest.getPhoneNumber()) &&
                !createOrUpdateStudentRequest.getPhoneNumber().matches("\\d{10}")) {
            errors.add("Invalid student phone number format");
        }

        // Parent validations
        CreateOrUpdateParentRequest parent = createOrUpdateStudentRequest.getParent();
        if (parent == null) {
            errors.add("Parent information is required");
        } else {
            if (StringUtils.isBlank(parent.getName())) {
                errors.add("Parent name is required");
            }
            if (StringUtils.isBlank(parent.getPhoneNumber())) {
                errors.add("Parent phone number is required");
            } else if (!parent.getPhoneNumber().matches("\\d{10}")) {
                errors.add("Invalid parent phone number format");
            }
        }

        if (!errors.isEmpty()) {
            messages.add(new ResponseMessage("Row " + rowNum + ": " + String.join(", ", errors)));
            return false;
        }
        return true;
    }
}