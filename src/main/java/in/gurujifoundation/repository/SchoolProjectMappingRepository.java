package in.gurujifoundation.repository;

import in.gurujifoundation.domain.SchoolProjectMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface SchoolProjectMappingRepository extends JpaRepository<SchoolProjectMapping, Long> {

    List<SchoolProjectMapping> findBySchoolId(Long schoolId);

    Optional<SchoolProjectMapping> findByProjectIdAndSchoolId(Long id, Long schoolId);
}
