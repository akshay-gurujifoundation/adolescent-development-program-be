package in.gurujifoundation.service.impl;


import in.gurujifoundation.domain.*;
import in.gurujifoundation.service.ExcelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ExcelServiceImpl implements ExcelService {

    @Override
    public Pair<HttpHeaders, InputStreamResource> createStudentExcelFile(List<Student> students, School school) {
        try {
            // Create Excel Workbook and Sheet
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Students");

            // Create Styles
            CellStyle boldStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);

            CellStyle borderedStyle = workbook.createCellStyle();
            borderedStyle.setBorderTop(BorderStyle.THIN);
            borderedStyle.setBorderBottom(BorderStyle.THIN);
            borderedStyle.setBorderLeft(BorderStyle.THIN);
            borderedStyle.setBorderRight(BorderStyle.THIN);

            CellStyle boldBorderedStyle = workbook.createCellStyle();
            boldBorderedStyle.cloneStyleFrom(borderedStyle);
            boldBorderedStyle.setFont(boldFont);

            // Add School Information at the Top
            Row schoolInfoRow1 = sheet.createRow(0);
            Cell schoolNameKeyCell = schoolInfoRow1.createCell(0);
            schoolNameKeyCell.setCellValue("School Name:");
            schoolNameKeyCell.setCellStyle(boldStyle);

            Cell schoolNameValueCell = schoolInfoRow1.createCell(1);
            schoolNameValueCell.setCellValue(school.getName());

            Row schoolInfoRow2 = sheet.createRow(1);
            Cell schoolIdKeyCell = schoolInfoRow2.createCell(0);
            schoolIdKeyCell.setCellValue("School ID:");
            schoolIdKeyCell.setCellStyle(boldStyle);

            Cell schoolIdValueCell = schoolInfoRow2.createCell(1);
            schoolIdValueCell.setCellValue(school.getId().toString());

            // Leave an empty row after school information
            sheet.createRow(2);

            // Create Header Row for Student Data
            Row headerRow = sheet.createRow(3); // Start after the empty row
            String[] columnHeaders = {"Name", "Date of Birth", "Address", "Phone Number", "Email"};
            for (int i = 0; i < columnHeaders.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnHeaders[i]);
                cell.setCellStyle(boldBorderedStyle); // Bold and bordered style
            }

            // Populate Rows with Student Data
            int rowNum = 4; // Start after the header row
            for (Student student : students) {
                Row row = sheet.createRow(rowNum++);
                Cell nameCell = row.createCell(0);
                nameCell.setCellValue(student.getName());
                nameCell.setCellStyle(borderedStyle);

                Cell dobCell = row.createCell(1);
                dobCell.setCellValue(student.getDob().toString());
                dobCell.setCellStyle(borderedStyle);

                Cell addressCell = row.createCell(2);
                addressCell.setCellValue(student.getAddress());
                addressCell.setCellStyle(borderedStyle);

                Cell phoneCell = row.createCell(3);
                phoneCell.setCellValue(student.getPhoneNumber());
                phoneCell.setCellStyle(borderedStyle);

                Cell emailCell = row.createCell(4);
                emailCell.setCellValue(student.getEmail());
                emailCell.setCellStyle(borderedStyle);
            }

            // Resize Columns to Fit Content
            for (int i = 0; i < columnHeaders.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write Excel Data to ByteArrayOutputStream
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();

            // Convert to InputStream for Download
            ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());

            // Set Headers for File Download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return Pair.of(headers, new InputStreamResource(inputStream));
        } catch (Exception ex) {
            log.error("Failed to convert the student data to Excel with the following error: {}", ex.getMessage());
            throw new RuntimeException("Failed to generate Excel file", ex);
        }
    }

    @Override
    public Pair<HttpHeaders, InputStreamResource> createStudentPerformanceUploadTemplate(List<SchoolProjectMapping> schoolProjectMappings, List<Project> projects, List<School> schools) {
        try {
            Workbook workbook = new XSSFWorkbook();
            addStudentPerformanceSheet(workbook);
            addSchoolProjectMappingSheet(workbook, schoolProjectMappings);
            addProjectTopicMappingSheet(workbook, projects);
            addSchoolStudentMappingSheet(workbook, schools);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student_performance.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return Pair.of(headers, new InputStreamResource(inputStream));
        } catch (Exception ex) {
            log.error("Failed to create the Excel template: {}", ex.getMessage());
            throw new RuntimeException("Failed to generate Excel file", ex);
        }
    }

    private void addStudentPerformanceSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Student Performance");
        createHeaderRow(sheet, new String[]{"Project Id", "School Id", "Student Id", "Topic Id", "Before Intervention Mark", "After Intervention Mark"});
    }

    private void addSchoolStudentMappingSheet(Workbook workbook, List<School> schools) {
        Sheet sheet = workbook.createSheet("School-Student Mapping");
        createHeaderRow(sheet, new String[]{"School Id", "School Name", "Student Id", "Student Name"});
        int rowNum = 1;
        for (School school : schools) {
            for (Student student : school.getStudents()) {
                Row row = sheet.createRow(rowNum++);
                createCell(row, 0, school.getId());
                createCell(row, 1, school.getName());
                createCell(row, 2, student.getId());
                createCell(row, 3, student.getName());
            }
        }
    }

    private void addProjectTopicMappingSheet(Workbook workbook, List<Project> projects) {
        Sheet sheet = workbook.createSheet("Project-Topic Mapping");
        createHeaderRow(sheet, new String[]{"Project Id", "Project Name", "Topic Id", "Topic Name"});
        int rowNum = 1;
        for (Project project : projects) {
            for (Topic topic : project.getTopics()) {
                Row row = sheet.createRow(rowNum++);
                createCell(row, 0, project.getId());
                createCell(row, 1, project.getName());
                createCell(row, 2, topic.getId());
                createCell(row, 3, topic.getName());
            }
        }
    }

    private void addSchoolProjectMappingSheet(Workbook workbook, List<SchoolProjectMapping> schoolProjectMappings) {
        Sheet sheet = workbook.createSheet("School-Project Mapping");
        createHeaderRow(sheet, new String[]{"Project Id", "Project Name", "School Id", "School Name"});
        int rowNum = 1;
        for (SchoolProjectMapping mapping : schoolProjectMappings) {
            Row row = sheet.createRow(rowNum++);
            createCell(row, 0, mapping.getProject().getId());
            createCell(row, 1, mapping.getProject().getName());
            createCell(row, 2, mapping.getSchool().getId());
            createCell(row, 3, mapping.getSchool().getName());
        }
    }

    private void createHeaderRow(Sheet sheet, String[] columnHeaders) {
        Row headerRow = sheet.createRow(0);
        CellStyle boldStyle = sheet.getWorkbook().createCellStyle();
        Font boldFont = sheet.getWorkbook().createFont();
        boldFont.setBold(true);
        boldStyle.setFont(boldFont);
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
            cell.setCellStyle(boldStyle);
        }
    }

    private void createCell(Row row, int column, Object value) {
        Cell cell = row.createCell(column);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value != null) {
            cell.setCellValue(value.toString());
        }
        CellStyle borderedStyle = row.getSheet().getWorkbook().createCellStyle();
        borderedStyle.setBorderTop(BorderStyle.THIN);
        borderedStyle.setBorderBottom(BorderStyle.THIN);
        borderedStyle.setBorderLeft(BorderStyle.THIN);
        borderedStyle.setBorderRight(BorderStyle.THIN);
        cell.setCellStyle(borderedStyle);
    }
}
