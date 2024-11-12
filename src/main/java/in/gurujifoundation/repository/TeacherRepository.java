package in.gurujifoundation.repository;

import in.gurujifoundation.domain.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    List<Teacher> findAllBySchoolId(Long schoolId);
}
