package in.gurujifoundation.repository;

import in.gurujifoundation.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}
