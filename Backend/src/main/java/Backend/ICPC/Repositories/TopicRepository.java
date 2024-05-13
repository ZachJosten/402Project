package Backend.ICPC.Repositories;

import Backend.ICPC.Models.User;
import Backend.ICPC.Models.Topics.Topic;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Topic findById(int id);

    List<Topic> findBySchool_Id(int schoolId);
}
