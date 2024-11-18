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
}
