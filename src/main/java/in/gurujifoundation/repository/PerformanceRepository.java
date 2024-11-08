package in.gurujifoundation.repository;

import in.gurujifoundation.domain.Performance;
import in.gurujifoundation.domain.Project;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
}
