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

    @Query("SELECT s FROM Student s " +
            "JOIN s.school sc " +
            "LEFT JOIN s.projects p " +
            "WHERE sc.id = :schoolId " +
            "AND (p.id IS NULL OR p.id != :projectId)")
    List<Student> findAllBySchoolIdAndExcludeProject(@Param("schoolId") Long schoolId,
                                                     @Param("projectId") Long projectId);


}
