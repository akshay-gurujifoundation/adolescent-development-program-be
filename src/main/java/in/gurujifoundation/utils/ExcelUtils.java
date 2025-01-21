package in.gurujifoundation.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExcelUtils {

    private ExcelUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Validates whether the uploaded file is an Excel file.
     *
     * @param file the uploaded file
     * @return true if the file is an Excel file, false otherwise
     */
    public static boolean isValidExcelFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
                contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || // .xlsx
                        contentType.equals("application/vnd.ms-excel")); // .xls
    }

    /**
     * Extracts the cell value as a String.
     *
     * @param cell the cell to extract the value from
     * @return the cell value as a String, or null if the cell is empty
     */
    public static String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> null;
        };
    }

    /**
     * Extracts the cell value as a LocalDate.
     *
     * @param cell the cell to extract the value from
     * @return the cell value as a LocalDate, or null if the cell is invalid
     */
    public static LocalDate getCellValueAsDate(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(cell.getStringCellValue(), formatter);
        } catch (Exception e) {
            return null;
        }
    }
}