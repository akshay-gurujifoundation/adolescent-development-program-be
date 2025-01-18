package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.Parent;
import in.gurujifoundation.domain.School;
import in.gurujifoundation.domain.Student;
import in.gurujifoundation.dto.BulkUploadResponse;
import in.gurujifoundation.dto.UploadStats;
import in.gurujifoundation.exception.EntityNotFoundException;
import in.gurujifoundation.exception.InternalServerException;
import in.gurujifoundation.mapper.ParentMapper;
import in.gurujifoundation.mapper.StudentMapper;
import in.gurujifoundation.repository.ParentRepository;
import in.gurujifoundation.repository.StudentRepository;
import in.gurujifoundation.request.CreateOrUpdateParentRequest;
import in.gurujifoundation.request.CreateOrUpdateStudentRequest;
import in.gurujifoundation.response.*;
import in.gurujifoundation.service.ExcelService;
import in.gurujifoundation.service.SchoolService;
import in.gurujifoundation.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final ParentRepository parentRepository;

    private final SchoolService schoolService;

    private final ExcelService excelService;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, ParentRepository parentRepository, SchoolService schoolService, ExcelService excelService) {
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.schoolService = schoolService;
        this.excelService = excelService;
    }

    @Override
    public ResponseMessage createStudent(CreateOrUpdateStudentRequest request) {
        try {
            SchoolDetails school = schoolService.getSchoolById(request.getSchoolId());
            Student student = StudentMapper.INSTANCE.toEntity(request, school);
            Parent parent = student.getParent();
            parentRepository.save(parent);
            studentRepository.save(student);
            return ResponseMessage.builder().message(ErrorCodeConstant.STUDENT_CREATED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while saving student with email: {}", request.getName(), e);
            throw new InternalServerException("Unexpected error occurred");
        }

    }

    @Override
    public StudentDetails getStudentById(Long id) {
        try {
            Student student = getStudent(id);
            log.debug("Successfully retrieved student details for id: {}", id);
            return StudentMapper.INSTANCE.toStudentDetails(student);
        } catch (Exception e) {
            log.error("Error occurred while fetching student from db for id: {} ", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public StudentsResponse getStudents(Long schoolId) {
        try {
            List<Student> students;
            if (schoolId != null) {
                students = studentRepository.findAllBySchoolId(schoolId);
            } else {
                students = studentRepository.findAll();
            }
            List<StudentDetails> studentDetails = StudentMapper.INSTANCE.toStudentsDetails(students);
            return StudentsResponse.builder().students(studentDetails).build();
        } catch (Exception e) {
            log.error("Error occurred while fetching students from db ", e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public ResponseMessage updateStudent(CreateOrUpdateStudentRequest request, Long id) {
        try {
            Student student = getStudent(id);
            School school = schoolService.getSchool(request.getSchoolId());
            Parent parent = student.getParent();
            ParentMapper.INSTANCE.updateParent(request.getParent(), parent);
            StudentMapper.INSTANCE.updateStudent(request, student, school, parent);
            studentRepository.save(student);
            return ResponseMessage.builder().message(ErrorCodeConstant.STUDENT_UPDATED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while updating student with email: {}", request.getEmail(), e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public ResponseMessage deleteStudent(Long id) {
        try {
            log.debug("Started deleting student with id: {}", id);
            studentRepository.deleteById(id);
            log.debug("Successfully deleted student with id: {}", id);
            return ResponseMessage.builder().message(ErrorCodeConstant.STUDENT_DELETED_SUCCESSFULLY).build();
        } catch (Exception e) {
            log.error("Error occurred while deleting student with id: {}", id, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public Student getStudent(Long id) {
        log.debug("Starting to fetch student details for id: {}", id);
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) {
            log.warn("No Student found for id: {}", id);
            throw new EntityNotFoundException(ErrorCodeConstant.STUDENT_DOES_NOT_EXIST);
        }
        return studentOptional.get();
    }

    @Override
    public List<Student> getStudentsByIds(Set<Long> ids) {
        return studentRepository.findAllById(ids);
    }

    @Override
    public void updateStudents(List<Student> students) {
        studentRepository.saveAll(students);
    }

    @Override
    public StudentsResponse getStudentsNotInSchoolProject(Long projectId, Long schoolId) {
        try {
            List<Student> studentsNotInSchoolProject = studentRepository.findStudentsNotInProjectBySchool(schoolId, projectId);
            List<StudentDetails> studentsDetails = StudentMapper.INSTANCE.toStudentsDetails(studentsNotInSchoolProject);
            return StudentsResponse.builder().students(studentsDetails).build();
        } catch (Exception e) {
            log.error("Error occurred while fetching student which are not assigned to project with id: {} for school Id {}", projectId, schoolId, e);
            throw new InternalServerException("Unexpected error occurred");
        }

    }

    @Override
    public StudentsResponse getStudentsInSchoolProject(Long projectId, Long schoolId) {
        try {
            List<Student> studentsNotInSchoolProject = studentRepository.findStudentsInProjectBySchool(schoolId, projectId);
            List<StudentDetails> studentsDetails = StudentMapper.INSTANCE.toStudentsDetails(studentsNotInSchoolProject);
            return StudentsResponse.builder().students(studentsDetails).build();
        } catch (Exception e) {
            log.error("Error occurred while fetching student which are assigned to project with id: {} for school Id {}", projectId, schoolId, e);
            throw new InternalServerException("Unexpected error occurred");
        }
    }

    @Override
    public Pair<HttpHeaders, InputStreamResource> getStudentExcelBySchoolId(Long schoolId) {
        School school = schoolService.getSchool(schoolId);
        List<Student> students = studentRepository.findAllBySchoolId(schoolId);
        return excelService.createStudentExcelFile(students, school);

    }

    /**
     * Generates an Excel template for student data upload.
     * The template includes columns for student and parent information.
     *
     * @return A Pair containing HTTP headers and the Excel template as an InputStreamResource
     * @throws InternalServerException if there's an error generating the template
     */
    @Override
    public Pair<HttpHeaders, InputStreamResource> getStudentUploadTemplate() {
        log.debug("Starting to generate student upload template");
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Student Template");

            // Define header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "Student Name", "Date of Birth", "Student Address", "Student Phone Number",
                    "Student Alternative Number", "Parent Name", "Parent Occupation", "Parent Phone Number"
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
            headersResponse.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Student_Template.xlsx");
            headersResponse.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            log.debug("Successfully generated student upload template");
            return Pair.of(headersResponse, new InputStreamResource(new ByteArrayInputStream(out.toByteArray())));
        } catch (IOException e) {
            log.error("IO error occurred while generating student upload template", e);
            throw new InternalServerException("Failed to generate Excel template due to IO error", e);
        } catch (Exception e) {
            log.error("Unexpected error occurred while generating student upload template", e);
            throw new InternalServerException("Failed to generate Excel template", e);
        }
    }

    @Override
    public BulkUploadResponse uploadStudentExcel(Long schoolId, MultipartFile file) {
        log.info("Processing student upload for school ID: {}", schoolId);

        School school = schoolService.getSchool(schoolId);

        List<ResponseMessage> messages = new ArrayList<>();
        UploadStats stats = new UploadStats();
        List<String> failedStudents = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // Skip header row
            int rowNum = 1;
            List<Student> validStudents = new ArrayList<>();
            List<Parent> validParents = new ArrayList<>();

            while (rowNum <= sheet.getLastRowNum()) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    rowNum++;
                    continue;
                }

                try {
                    CreateOrUpdateStudentRequest createOrUpdateStudentRequest = extractStudentFromRow(row);
                    String studentName = createOrUpdateStudentRequest.getName();
                    
                    if (validateStudent(createOrUpdateStudentRequest, messages, rowNum)) {
                        Student student = StudentMapper.INSTANCE.createOrUpdateStudentRequestToEntity(createOrUpdateStudentRequest, school);
                        Parent parent = student.getParent();
                        validParents.add(parent);
                        validStudents.add(student);
                        stats.incrementSuccessCount();
                    } else {
                        stats.incrementFailureCount();
                        if (studentName != null && !studentName.trim().isEmpty()) {
                            failedStudents.add(studentName);
                        } else {
                            failedStudents.add("Row " + rowNum + " (No Name)");
                        }
                    }
                } catch (Exception e) {
                    log.error("Error processing row {}: {}", rowNum, e.getMessage());
                    messages.add(new ResponseMessage("Error in row " + rowNum + ": " + e.getMessage()));
                    stats.incrementFailureCount();
                    String studentName = getCellValueAsString(row.getCell(0));
                    failedStudents.add(studentName != null ? studentName : "Row " + rowNum + " (No Name)");
                }
                rowNum++;
            }

            // Batch save parents first, then students
            if (!validStudents.isEmpty()) {
                parentRepository.saveAll(validParents);
                studentRepository.saveAll(validStudents);
                log.info("Successfully saved {} students for school ID: {}", validStudents.size(), schoolId);
            }

        } catch (IOException e) {
            log.error("Failed to process Excel file", e);
            throw new InternalServerException("Failed to process Excel file: " + e.getMessage());
        }

        stats.setFailedStudents(failedStudents);
        return new BulkUploadResponse(messages, stats);
    }

    private CreateOrUpdateStudentRequest extractStudentFromRow(Row row) {
        CreateOrUpdateParentRequest createOrUpdateParentRequest = CreateOrUpdateParentRequest.builder()
                .name(getCellValueAsString(row.getCell(5)))
                .occupation(getCellValueAsString(row.getCell(6)))
                .phoneNumber(getCellValueAsString(row.getCell(7)))
                .build();
        return CreateOrUpdateStudentRequest.builder()
                .name(getCellValueAsString(row.getCell(0)))
                .dob(getCellValueAsDate(row.getCell(1)))
                .address(getCellValueAsString(row.getCell(2)))
                .phoneNumber(getCellValueAsString(row.getCell(3)))
                .alternativeNumber(getCellValueAsString(row.getCell(4)))
                .parent(createOrUpdateParentRequest)
                .build();
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> null;
        };
    }

    private LocalDate getCellValueAsDate(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
            return LocalDate.parse(cell.getStringCellValue());
        } catch (Exception e) {
            return null;
        }
    }

    private boolean validateStudent(CreateOrUpdateStudentRequest createOrUpdateStudentRequest, List<ResponseMessage> messages, int rowNum) {
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
        if (StringUtils.isNotBlank(createOrUpdateStudentRequest.getPhoneNumber()) 
                && !createOrUpdateStudentRequest.getPhoneNumber().matches("\\d{10}")) {
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
