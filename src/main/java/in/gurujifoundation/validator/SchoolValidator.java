package in.gurujifoundation.validator;

import in.gurujifoundation.request.CreateOrUpdateSchoolRequest;
import in.gurujifoundation.response.ResponseMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SchoolValidator {

    /**
     * Validates the details of a school.
     *
     * @param createOrUpdateSchoolRequest the request object containing school details
     * @param messages                    the list to store validation error messages
     * @param rowNum                      the row number for reference in error messages
     * @return true if the school details are valid, false otherwise
     */
    public static boolean validateSchool(CreateOrUpdateSchoolRequest createOrUpdateSchoolRequest,
                                         List<ResponseMessage> messages, int rowNum) {
        List<String> errors = new ArrayList<>();

        // School validations
        if (StringUtils.isBlank(createOrUpdateSchoolRequest.getName())) {
            errors.add("School name is required");
        }

        if (StringUtils.isBlank(createOrUpdateSchoolRequest.getAddress())) {
            errors.add("Address is required");
        }

        if (StringUtils.isNotBlank(createOrUpdateSchoolRequest.getPhoneNumber())
                && !createOrUpdateSchoolRequest.getPhoneNumber().matches("\\d{10}")) {
            errors.add("Invalid school phone number format");
        }

        if (StringUtils.isNotBlank(createOrUpdateSchoolRequest.getPrincipalContactNo())
                && !createOrUpdateSchoolRequest.getPrincipalContactNo().matches("\\d{10}")) {
            errors.add("Invalid principal contact number format");
        }

        if (!errors.isEmpty()) {
            messages.add(new ResponseMessage("Row " + rowNum + ": " + String.join(", ", errors)));
            return false;
        }
        return true;
    }
}