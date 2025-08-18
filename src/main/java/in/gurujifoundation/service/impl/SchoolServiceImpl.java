package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.School;
import in.gurujifoundation.dto.BulkUploadResponse;
import in.gurujifoundation.dto.ExcelValidationResult;
import in.gurujifoundation.dto.RowValidationResult;
import in.gurujifoundation.dto.UploadStats;
import in.gurujifoundation.exception.BadRequestException;
import in.gurujifoundation.exception.EntityNotFoundException;
import in.gurujifoundation.exception.InternalServerException;
import in.gurujifoundation.mapper.SchoolMapper;
import in.gurujifoundation.repository.SchoolRepository;
import in.gurujifoundation.request.CreateOrUpdateSchoolRequest;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.response.SchoolDetails;
import in.gurujifoundation.response.SchoolsResponse;
import in.gurujifoundation.service.SchoolService;
import in.gurujifoundation.utils.ExcelUtils;
import in.gurujifoundation.validator.SchoolValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    @Autowired
    public SchoolServiceImpl(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @Override
    public ResponseMessage createSchool(CreateOrUpdateSchoolRequest createSchoolRequest) {
        try {
            log.debug("Started creating school with name: {}", createSchoolRequest.getName());

            // Check if school with the same name already exists
            Optional<School> existingSchool = schoolRepository.findByNameIgnoreCase(createSchoolRequest.getName());
            if (existingSchool.isPresent()) {
                log.warn("School with name '{}' already exists", createSchoolRequest.getName());
                throw new BadRequestException(ErrorCodeConstant.SCHOOL_NAME_ALREADY_EXISTS);
            }

            School school = SchoolMapper.INSTANCE.mapToEntity(createSchoolRequest);
            schoolRepository.save(school);
            log.debug("Successfully created school with name: {}", createSchoolRequest.getName());
            return ResponseMessage.builder().message(ErrorCodeConstant.SCHOOL_CREATED_SUCCESSFULLY).build();
        } catch (BadRequestException e) {
            // Re-throw BadRequestException to be handled by the global exception handler
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while saving school with name: {}", createSchoolRequest.getName(), e);
            throw new InternalServerException("Unexpected error occurred");
        }

    }

    @Override
    public SchoolDetails getSchoolById(Long id) {
        try {
            log.debug("Started fetching school with id: {}", id);
            School school = getSchool(id);
            log.debug("Successfully retrieved school details for id: {}", id);
            return SchoolMapper.INSTANCE.mapToSchoolDetails(school);
        } catch (Exception e) {
            log.error("Error occurred while fetching school from db for id: {} ", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public SchoolsResponse getSchools() {
        try {
            log.debug("Started fetching all schools");
            List<School> schools = schoolRepository.findAll();
            List<SchoolDetails> schoolDetails = SchoolMapper.INSTANCE.mapToSchoolDetailsList(schools);
            log.debug("Successfully fetched all schools");
            return SchoolsResponse.builder().schools(schoolDetails).build();
        } catch (Exception e) {
            log.error("Error occurred while fetching schools from db ", e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public ResponseMessage updateSchool(CreateOrUpdateSchoolRequest updateSchoolRequest, Long id) {
        try {
            log.debug("Started updating school with id: {}", id);
            School school = getSchool(id);

            // Check if the name is being changed and if a school with the new name already exists
            if (!school.getName().equalsIgnoreCase(updateSchoolRequest.getName())) {
                Optional<School> existingSchool = schoolRepository.findByNameIgnoreCase(updateSchoolRequest.getName());
                if (existingSchool.isPresent() && !existingSchool.get().getId().equals(id)) {
                    log.warn("School with name '{}' already exists", updateSchoolRequest.getName());
                    throw new BadRequestException(ErrorCodeConstant.SCHOOL_NAME_ALREADY_EXISTS);
                }
            }

            SchoolMapper.INSTANCE.updateSchool(updateSchoolRequest, school);
            schoolRepository.save(school);
            log.debug("Successfully updated school with id: {}", id);
            return ResponseMessage.builder().message(ErrorCodeConstant.SCHOOL_UPDATED_SUCCESSFULLY).build();
        } catch (BadRequestException e) {
            // Re-throw BadRequestException to be handled by the global exception handler
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating school with email: {}", updateSchoolRequest.getName(), e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public School getSchool(Long id) {
        log.debug("Starting to fetch school details for id: {}", id);
        Optional<School> schoolOptional = schoolRepository.findById(id);
        if (schoolOptional.isEmpty()) {
            log.warn("No School found for id: {}", id);
            throw new EntityNotFoundException(ErrorCodeConstant.SCHOOL_DOES_NOT_EXIST);
        }
        return schoolOptional.get();
    }

    @Override
    public ResponseMessage deleteSchool(Long id) {
        try {
            log.debug("Started deleting school with id: {}", id);
            schoolRepository.deleteById(id);

            log.debug("Successfully deleted school with id: {}", id);
            return ResponseMessage.builder().message(ErrorCodeConstant.SCHOOL_DELETED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while deleting school with id: {}", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public SchoolsResponse getUnAssignSchoolsForProject(Long projectId) {
        try {
            log.debug("Started fetching unassigned schools to project");
            List<School> schools = schoolRepository.findUnAssignedSchoolForProject(projectId);
            List<SchoolDetails> schoolDetails = SchoolMapper.INSTANCE.mapToSchoolDetailsList(schools);
            log.debug("Successfully fetched unassigned schools");
            return SchoolsResponse.builder().schools(schoolDetails).build();
        } catch (Exception e) {
            log.error("Error occurred while fetching unassigned schools from db for project id : {} ", projectId, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public List<School> getAllSchools() {
        return schoolRepository.findAll();
    }

    /**
     * Generates an Excel template for school data upload.
     * The template includes columns for school and management information.
     *
     * @return A Pair containing HTTP headers and the Excel template as an InputStreamResource
     * @throws InternalServerException if there's an error generating the template
     */
    @Override
    public Pair<HttpHeaders, InputStreamResource> getSchoolUploadTemplate() {
        log.debug("Starting to generate school upload template");
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("School Template");

            // Define header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "School Name", "School Address", "Phone Number", "Principal Name",
                    "Principal Contact Number", "Managing Trustee", "Trustee Contact Info", "Website"
            };

            // Create header cells
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            // Autosize columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the workbook to a byte array
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            // Create HTTP headers
            HttpHeaders headersResponse = new HttpHeaders();
            headersResponse.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=School_Template.xlsx");
            headersResponse.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            log.debug("Successfully generated school upload template");
            return Pair.of(headersResponse, new InputStreamResource(new ByteArrayInputStream(out.toByteArray())));
        } catch (IOException e) {
            log.error("IO error occurred while generating school upload template", e);
            throw new InternalServerException("Failed to generate Excel template due to IO error", e);
        } catch (Exception e) {
            log.error("Unexpected error occurred while generating school upload template", e);
            throw new InternalServerException("Failed to generate Excel template", e);
        }
    }

    @Override
    public BulkUploadResponse uploadSchoolExcel(MultipartFile file) {
        log.info("Processing school upload.");

        ExcelValidationResult validationResult = new ExcelValidationResult();
        List<School> validSchools = new ArrayList<>();
        // Set to track school names in the Excel file to check for duplicates
        Set<String> schoolNamesInFile = new HashSet<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // Skip header row
            int rowNum = 1;

            while (rowNum <= sheet.getLastRowNum()) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    rowNum++;
                    continue;
                }

                try {
                    CreateOrUpdateSchoolRequest createOrUpdateSchoolRequest = extractSchoolFromRow(row);

                    // Validate the school with detailed validation
                    RowValidationResult rowResult = SchoolValidator.validateSchoolDetailed(createOrUpdateSchoolRequest, rowNum);

                    // Add the validation result to the overall results
                    validationResult.addRowResult(rowResult);

                    // If valid, check for uniqueness and add to the list of schools to save
                    if (!rowResult.hasErrors()) {
                        String schoolName = createOrUpdateSchoolRequest.getName().toLowerCase();

                        // Check if school with the same name already exists in the database
                        Optional<School> existingSchool = schoolRepository.findByNameIgnoreCase(schoolName);

                        // Check if school with the same name already exists in the current file
                        boolean duplicateInFile = !schoolNamesInFile.add(schoolName);

                        if (existingSchool.isPresent()) {
                            // Add a validation error for duplicate school name in database
                            rowResult.addError("Name", ErrorCodeConstant.SCHOOL_NAME_ALREADY_EXISTS + " in database");
                            validationResult.addRowResult(rowResult);
                        } else if (duplicateInFile) {
                            // Add a validation error for duplicate school name in the current file
                            rowResult.addError("Name", ErrorCodeConstant.SCHOOL_NAME_ALREADY_EXISTS + " in current file");
                            validationResult.addRowResult(rowResult);
                        } else {
                            School school = SchoolMapper.INSTANCE.mapToEntity(createOrUpdateSchoolRequest);
                            validSchools.add(school);
                        }
                    }
                } catch (Exception e) {
                    log.error("Error processing row {}: {}", rowNum, e.getMessage());

                    // Create a validation result for the exception
                    RowValidationResult errorResult = RowValidationResult.builder()
                            .rowNumber(rowNum)
                            .schoolName(row != null ? ExcelUtils.getCellValueAsString(row.getCell(0)) : null)
                            .build();
                    errorResult.addError("Processing Error", e.getMessage());

                    // Add the error result to the overall results
                    validationResult.addRowResult(errorResult);
                }
                rowNum++;
            }

            // Only save schools if there are no validation errors in any row
            if (!validSchools.isEmpty() && validationResult.getStats().getFailureCount() == 0) {
                schoolRepository.saveAll(validSchools);
                log.info("Successfully saved {} schools.", validSchools.size());
            } else if (validationResult.getStats().getFailureCount() > 0) {
                log.info("Not saving any schools because there are validation errors in the file.");
            }

        } catch (IOException e) {
            log.error("Failed to process Excel file", e);
            throw new InternalServerException("Failed to process Excel file: " + e.getMessage());
        }

        // Convert the validation results to a BulkUploadResponse
        return validationResult.toBulkUploadResponse();
    }

    @Override
    public Pair<HttpHeaders, InputStreamResource> exportSchools() {
        List<School> schools = schoolRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Schools");

            // Header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "ID", "Name", "Address", "Phone Number", "Principal Name", "Principal Contact No",
                    "Managing Trustee", "Trustee Contact Info", "Website", "Total Teachers", "Total Students"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }

            // Data rows
            int rowIndex = 1;
            for (School school : schools) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(school.getId());
                row.createCell(1).setCellValue(school.getName());
                row.createCell(2).setCellValue(school.getAddress());
                row.createCell(3).setCellValue(school.getPhoneNumber());
                row.createCell(4).setCellValue(school.getPrincipalName());
                row.createCell(5).setCellValue(school.getPrincipalContactNo());
                row.createCell(6).setCellValue(school.getManagingTrustee());
                row.createCell(7).setCellValue(school.getTrusteeContactInfo());
                row.createCell(8).setCellValue(school.getWebsite());
                row.createCell(9).setCellValue(school.getTeachers().size());
                row.createCell(10).setCellValue(school.getStudents().size());
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
            InputStreamResource resource = new InputStreamResource(inputStream);

            HttpHeaders headersResponse = new HttpHeaders();
            headersResponse.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=schools.xlsx");
            headersResponse.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return Pair.of(headersResponse, resource);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate school upload template", e);
        }
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private CreateOrUpdateSchoolRequest extractSchoolFromRow(Row row) {
        return CreateOrUpdateSchoolRequest.builder()
                .name(ExcelUtils.getCellValueAsString(row.getCell(0)))
                .address(ExcelUtils.getCellValueAsString(row.getCell(1)))
                .phoneNumber(ExcelUtils.getCellValueAsString(row.getCell(2)))
                .principalName(ExcelUtils.getCellValueAsString(row.getCell(3)))
                .principalContactNo(ExcelUtils.getCellValueAsString(row.getCell(4)))
                .managingTrustee(ExcelUtils.getCellValueAsString(row.getCell(5)))
                .trusteeContactInfo(ExcelUtils.getCellValueAsString(row.getCell(6)))
                .website(ExcelUtils.getCellValueAsString(row.getCell(7)))
                .build();
    }
}
