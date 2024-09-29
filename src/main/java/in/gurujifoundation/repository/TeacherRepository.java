package in.gurujifoundation.repository;

import in.gurujifoundation.domain.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
