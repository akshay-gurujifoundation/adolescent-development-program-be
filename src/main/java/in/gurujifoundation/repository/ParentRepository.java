package in.gurujifoundation.repository;

import in.gurujifoundation.domain.Parent;
import in.gurujifoundation.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ParentRepository extends JpaRepository<Parent, Long> {

}
