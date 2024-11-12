package in.gurujifoundation.repository;

import in.gurujifoundation.domain.Parent;
import in.gurujifoundation.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TopicRepository extends JpaRepository<Topic, Long> {

}
