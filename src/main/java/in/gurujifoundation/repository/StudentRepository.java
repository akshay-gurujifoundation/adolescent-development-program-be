package in.gurujifoundation.repository;

import in.gurujifoundation.domain.School;
import in.gurujifoundation.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StudentRepository extends JpaRepository<Student, Long> {

}
