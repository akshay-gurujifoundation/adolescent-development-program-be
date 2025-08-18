package in.gurujifoundation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the validation result for a single row in an Excel file.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RowValidationResult {
    private int rowNumber;
    private String schoolName;
    
    @Builder.Default
    private List<RowValidationError> errors = new ArrayList<>();
    
    /**
     * Adds a validation error for a specific field.
     *
     * @param fieldName    the name of the field that failed validation
     * @param errorMessage the error message for the validation failure
     */
    public void addError(String fieldName, String errorMessage) {
        errors.add(RowValidationError.builder()
                .rowNumber(rowNumber)
                .fieldName(fieldName)
                .errorMessage(errorMessage)
                .build());
    }
    
    /**
     * Checks if the row has any validation errors.
     *
     * @return true if the row has validation errors, false otherwise
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}