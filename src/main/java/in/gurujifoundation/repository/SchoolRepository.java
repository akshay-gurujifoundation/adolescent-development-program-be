package in.gurujifoundation.repository;

import in.gurujifoundation.domain.School;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface SchoolRepository extends JpaRepository<School, Long> {
}
