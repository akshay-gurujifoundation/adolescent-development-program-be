package in.gurujifoundation.repository;

import in.gurujifoundation.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllBySchoolId(Long schoolId);

    @Query(value = """
           SELECT s.* 
           FROM student s
           WHERE s.id NOT IN (
               SELECT student_id
               FROM school_project_student_mapping
               WHERE school_project_id = :schoolProjectId
           )
           """, nativeQuery = true)
    List<Student> findStudentsNotInSchoolProject(@Param("schoolProjectId") Long schoolProjectId);



}
