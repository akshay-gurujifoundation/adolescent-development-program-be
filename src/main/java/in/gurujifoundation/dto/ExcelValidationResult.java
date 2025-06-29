package in.gurujifoundation.dto;

import in.gurujifoundation.response.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the validation results for all rows in an Excel file.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelValidationResult {

    @Builder.Default
    private List<RowValidationResult> rowResults = new ArrayList<>();

    @Builder.Default
    private UploadStats stats = new UploadStats();

    /**
     * Adds a validation result for a row.
     *
     * @param rowResult the validation result for the row
     */
    public void addRowResult(RowValidationResult rowResult) {
        rowResults.add(rowResult);

        if (rowResult.hasErrors()) {
            stats.incrementFailureCount();
            if (rowResult.getSchoolName() != null && !rowResult.getSchoolName().trim().isEmpty()) {
                stats.getFailedItems().add(rowResult.getSchoolName() + " (Row " + rowResult.getRowNumber() + ")");
            } else {
                stats.getFailedItems().add("Row " + rowResult.getRowNumber() + " (No Name)");
            }
        } else {
            stats.incrementSuccessCount();
        }
    }

    /**
     * Converts the validation results to a list of ResponseMessage objects.
     *
     * @return a list of ResponseMessage objects
     */
    public List<ResponseMessage> toResponseMessages() {
        List<ResponseMessage> messages = new ArrayList<>();

        // Add detailed validation error messages
        for (RowValidationResult rowResult : rowResults) {
            if (rowResult.hasErrors()) {
                for (RowValidationError error : rowResult.getErrors()) {
                    String message = String.format("Row %d (%s): %s - %s", 
                            error.getRowNumber(),
                            rowResult.getSchoolName() != null ? rowResult.getSchoolName() : "No Name",
                            error.getFieldName(),
                            error.getErrorMessage());
                    messages.add(new ResponseMessage(message));
                }
            }
        }

        // Add summary messages
        if (stats.getSuccessCount() > 0) {
            messages.add(new ResponseMessage("Successfully processed " + stats.getSuccessCount() + " schools"));
        }
        if (stats.getFailureCount() > 0) {
            messages.add(new ResponseMessage("Failed to process " + stats.getFailureCount() + " schools due to validation errors"));
            messages.add(new ResponseMessage("No schools were saved because there were validation errors in the file"));
        }

        return messages;
    }

    /**
     * Creates a BulkUploadResponse from the validation results.
     *
     * @return a BulkUploadResponse
     */
    public BulkUploadResponse toBulkUploadResponse() {
        return new BulkUploadResponse(toResponseMessages(), stats);
    }
}
