package in.gurujifoundation.service;

import in.gurujifoundation.domain.School;
import in.gurujifoundation.domain.Student;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface ExcelService {

    Pair<HttpHeaders, InputStreamResource> createStudentExcelFile(List<Student> students, School school);
}
