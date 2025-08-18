package in.gurujifoundation.validator;

import in.gurujifoundation.dto.RowValidationResult;
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
     * @param messages                    the list to store validation error messages (for backward compatibility)
     * @param rowNum                      the row number for reference in error messages
     * @return true if the school details are valid, false otherwise
     */
    public static boolean validateSchool(CreateOrUpdateSchoolRequest createOrUpdateSchoolRequest,
                                         List<ResponseMessage> messages, int rowNum) {
        RowValidationResult result = validateSchoolDetailed(createOrUpdateSchoolRequest, rowNum);

        // For backward compatibility, add messages to the provided list
        if (result.hasErrors()) {
            for (var error : result.getErrors()) {
                messages.add(new ResponseMessage("Row " + rowNum + ": " + error.getFieldName() + " - " + error.getErrorMessage()));
            }
            return false;
        }
        return true;
    }

    /**
     * Validates the details of a school and returns detailed validation results.
     *
     * @param createOrUpdateSchoolRequest the request object containing school details
     * @param rowNum                      the row number for reference in error messages
     * @return a RowValidationResult containing detailed validation results
     */
    public static RowValidationResult validateSchoolDetailed(CreateOrUpdateSchoolRequest createOrUpdateSchoolRequest, int rowNum) {
        RowValidationResult result = RowValidationResult.builder()
                .rowNumber(rowNum)
                .schoolName(createOrUpdateSchoolRequest.getName())
                .build();

        // School name validation
        if (StringUtils.isBlank(createOrUpdateSchoolRequest.getName())) {
            result.addError("School Name", "School name is required");
        }

        // Address validation
        if (StringUtils.isBlank(createOrUpdateSchoolRequest.getAddress())) {
            result.addError("Address", "Address is required");
        }

        // Phone number validation
        if (StringUtils.isBlank(createOrUpdateSchoolRequest.getPhoneNumber())) {
            result.addError("Phone Number", "Phone number is required");
        } else if (!createOrUpdateSchoolRequest.getPhoneNumber().matches("\\d{10}")) {
            result.addError("Phone Number", "Invalid school phone number format (must be 10 digits)");
        }

        // Principal name validation
        if (StringUtils.isBlank(createOrUpdateSchoolRequest.getPrincipalName())) {
            result.addError("Principal Name", "Principal name is required");
        }

        // Principal contact number validation
        if (StringUtils.isBlank(createOrUpdateSchoolRequest.getPrincipalContactNo())) {
            result.addError("Principal Contact Number", "Principal contact number is required");
        } else if (!createOrUpdateSchoolRequest.getPrincipalContactNo().matches("\\d{10}")) {
            result.addError("Principal Contact Number", "Invalid principal contact number format (must be 10 digits)");
        }

        // Managing trustee validation
        if (StringUtils.isBlank(createOrUpdateSchoolRequest.getManagingTrustee())) {
            result.addError("Managing Trustee", "Managing trustee is required");
        }

        // Trustee contact info validation
        if (StringUtils.isBlank(createOrUpdateSchoolRequest.getTrusteeContactInfo())) {
            result.addError("Trustee Contact Info", "Trustee contact information is required");
        } else if (!createOrUpdateSchoolRequest.getTrusteeContactInfo().matches("\\d{10}")) {
            result.addError("Trustee Contact Info", "Invalid trustee contact number format (must be 10 digits)");
        }

        return result;
    }
}
