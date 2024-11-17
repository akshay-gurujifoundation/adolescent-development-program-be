package in.gurujifoundation.repository;

import in.gurujifoundation.domain.Project;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ProjectRepository extends JpaRepository<Project, Long> {


    @Query("SELECT p FROM Project p JOIN p.schoolProjects sp WHERE sp.school.id = :schoolId")
    List<Project> findAllBySchoolId(@Param("schoolId") Long schoolId);
}
