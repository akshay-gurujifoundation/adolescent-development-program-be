package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.School;
import in.gurujifoundation.dto.BulkUploadResponse;
import in.gurujifoundation.dto.UploadStats;
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
import java.util.List;
import java.util.Optional;

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
            School school = SchoolMapper.INSTANCE.mapToEntity(createSchoolRequest);
            schoolRepository.save(school);
            log.debug("Successfully created school with name: {}", createSchoolRequest.getName());
            return ResponseMessage.builder().message(ErrorCodeConstant.SCHOOL_CREATED_SUCCESSFULLY).build();
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
            SchoolMapper.INSTANCE.updateSchool(updateSchoolRequest, school);
            schoolRepository.save(school);
            log.debug("Successfully updated school with id: {}", id);
            return ResponseMessage.builder().message(ErrorCodeConstant.SCHOOL_UPDATED_SUCCESSFULLY).build();
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
            log.error("Error occurred while deleting teacher with id: {}", id, e);
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

        List<ResponseMessage> messages = new ArrayList<>();
        UploadStats stats = new UploadStats();
        List<String> failedSchools = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // Skip header row
            int rowNum = 1;
            List<School> validSchools = new ArrayList<>();

            while (rowNum <= sheet.getLastRowNum()) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    rowNum++;
                    continue;
                }

                try {
                    CreateOrUpdateSchoolRequest createOrUpdateSchoolRequest = extractSchoolFromRow(row);
                    String schoolName = createOrUpdateSchoolRequest.getName();

                    if (SchoolValidator.validateSchool(createOrUpdateSchoolRequest, messages, rowNum)) {
                        School school = SchoolMapper.INSTANCE.mapToEntity(createOrUpdateSchoolRequest);
                        validSchools.add(school);
                        stats.incrementSuccessCount();
                    } else {
                        stats.incrementFailureCount();
                        if (schoolName != null && !schoolName.trim().isEmpty()) {
                            failedSchools.add(schoolName);
                        } else {
                            failedSchools.add("Row " + rowNum + " (No Name)");
                        }
                    }
                } catch (Exception e) {
                    log.error("Error processing row {}: {}", rowNum, e.getMessage());
                    messages.add(new ResponseMessage("Error in row " + rowNum + ": " + e.getMessage()));
                    stats.incrementFailureCount();
                    String schoolName = ExcelUtils.getCellValueAsString(row.getCell(0));
                    failedSchools.add(schoolName != null ? schoolName : "Row " + rowNum + " (No Name)");
                }
                rowNum++;
            }

            // Batch save schools
            if (!validSchools.isEmpty()) {
                schoolRepository.saveAll(validSchools);
                log.info("Successfully saved {} schools.", validSchools.size());
            }

        } catch (IOException e) {
            log.error("Failed to process Excel file", e);
            throw new InternalServerException("Failed to process Excel file: " + e.getMessage());
        }

        stats.setFailedItems(failedSchools);
        return new BulkUploadResponse(messages, stats);
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
