package in.gurujifoundation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a validation error for a specific row and field in an Excel file.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RowValidationError {
    private int rowNumber;
    private String fieldName;
    private String errorMessage;
}