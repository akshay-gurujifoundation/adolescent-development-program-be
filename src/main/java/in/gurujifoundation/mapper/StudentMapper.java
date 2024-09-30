package in.gurujifoundation.mapper;

import in.gurujifoundation.domain.School;
import in.gurujifoundation.domain.Student;
import in.gurujifoundation.request.CreateOrUpdateStudentRequest;
import in.gurujifoundation.response.SchoolDetails;
import in.gurujifoundation.response.StudentDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    ParentMapper PARENT_MAPPER = ParentMapper.INSTANCE;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "dob", source = "request.dob")
    @Mapping(target = "address", source = "request.address")
    @Mapping(target = "phoneNumber", source = "request.phoneNumber")
    @Mapping(target = "alternativeNumber", source = "request.alternativeNumber")
    @Mapping(target = "email", source = "request.email")
    @Mapping(target = "school", source = "school")
    @Mapping(target = "parent", expression = "java(PARENT_MAPPER.toParent(request.getParent()))")
    Student toEntity(CreateOrUpdateStudentRequest request, SchoolDetails school);


    @Mapping(target = "id", source = "student.id")
    @Mapping(target = "name", source = "student.name")
    @Mapping(target = "dob", source = "student.dob")
    @Mapping(target = "address", source = "student.address")
    @Mapping(target = "phoneNumber", source = "student.phoneNumber")
    @Mapping(target = "alternativeNumber", source = "student.alternativeNumber")
    @Mapping(target = "email", source = "student.email")
    @Mapping(target = "parent", source = "student.parent")
    StudentDetails toStudentDetails(Student student);

    List<StudentDetails> toStudentsDetails(List<Student> students);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "studentDetails.name")
    @Mapping(target = "dob", source = "studentDetails.dob")
    @Mapping(target = "address", source = "studentDetails.address")
    @Mapping(target = "phoneNumber", source = "studentDetails.phoneNumber")
    @Mapping(target = "alternativeNumber", source = "studentDetails.alternativeNumber")
    @Mapping(target = "email", source = "studentDetails.email")
    @Mapping(target = "parent", source = "studentDetails.parent")
    @Mapping(target = "school", source = "school")
    void updateStudent(CreateOrUpdateStudentRequest studentDetails, @MappingTarget Student student, School school);
}
