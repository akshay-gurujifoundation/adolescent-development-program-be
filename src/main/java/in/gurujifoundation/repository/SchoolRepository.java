package in.gurujifoundation.repository;

import in.gurujifoundation.domain.School;
import in.gurujifoundation.domain.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface SchoolRepository extends JpaRepository<School, Long> {

    @Query("SELECT s FROM School s WHERE s.id NOT IN (SELECT spm.school.id FROM SchoolProjectMapping spm WHERE spm.project.id = :projectId)")
    List<School> findUnAssignedSchoolForProject(@Param("projectId") Long projectId);


}
