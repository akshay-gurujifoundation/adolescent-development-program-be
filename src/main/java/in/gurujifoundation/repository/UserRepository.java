package in.gurujifoundation.repository;

import in.gurujifoundation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
