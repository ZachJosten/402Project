package Backend.ICPC.Repositories;

import Backend.ICPC.Models.Levels.LevelKey;
import Backend.ICPC.Models.LevelsTopics.LevelTopic;
import Backend.ICPC.Models.LevelsTopics.LevelTopicKey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelTopicRepository extends JpaRepository<LevelTopic, Long> {
    LevelTopic findById(LevelTopicKey id);

    List<LevelTopic> findByIdLevelId(LevelKey lid);

    List<LevelTopic> findByIdTid(int tid);
}
