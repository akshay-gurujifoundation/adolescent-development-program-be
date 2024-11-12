package in.gurujifoundation.repository;

import in.gurujifoundation.domain.ProjectCoordinator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ProjectCoordinatorRepository extends JpaRepository<ProjectCoordinator, Long> {

}
