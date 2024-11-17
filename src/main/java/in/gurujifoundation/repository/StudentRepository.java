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
                    INNER JOIN school sc ON s.school_id = sc.id
           WHERE sc.id = :schoolId
             AND s.id NOT IN (
                   SELECT spsm.student_id
                   FROM school_project_mapping spm
                            INNER JOIN school_project_student_mapping spsm 
                            ON spsm.school_project_id = spm.id
                   WHERE spm.project_id = :projectId
                     AND spm.school_id = :schoolId
           )
           """, nativeQuery = true)
    List<Student> findStudentsNotInProjectBySchool(
            @Param("schoolId") Long schoolId,
            @Param("projectId") Long projectId);



}
