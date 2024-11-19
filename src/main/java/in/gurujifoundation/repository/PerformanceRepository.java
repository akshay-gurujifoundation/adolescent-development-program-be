package in.gurujifoundation.repository;

import in.gurujifoundation.domain.Performance;
import in.gurujifoundation.domain.Project;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Performance p WHERE p.student.id = :studentId AND p.topic.id = :topicId")
    void deleteByStudentIdAndTopicId(@Param("studentId") Long studentId, @Param("topicId") Long topicId);

    @Query(value = "SELECT * FROM performance p " +
            "WHERE p.student_id IN ( " +
            "   SELECT spsm.student_id " +
            "   FROM school_project_mapping spm " +
            "   INNER JOIN school_project_student_mapping spsm " +
            "   ON spm.id = spsm.school_project_id " +
            "   WHERE spm.school_id = :schoolId AND spm.project_id = :projectId" +
            ")", nativeQuery = true)
    List<Performance> findBySchoolIdAndProjectId(@Param("schoolId") Long schoolId, @Param("projectId") Long projectId);

}
