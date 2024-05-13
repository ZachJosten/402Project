package Backend.ICPC.Controllers;

import Backend.ICPC.Models.LevelsTopics.LevelTopic;
import Backend.ICPC.Models.LevelsTopics.LevelTopicKey;
import Backend.ICPC.Models.Topics.Topic;
import Backend.ICPC.Models.Levels.LevelKey;
import Backend.ICPC.Models.Levels.Levels;
import Backend.ICPC.Repositories.LevelRepository;
import Backend.ICPC.Repositories.LevelTopicRepository;
import Backend.ICPC.Repositories.TopicRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "LevelTopic controller", description = "REST API related to Level Topic Entity", tags = "LevelTopicController")
@RestController
public class LevelTopicController {

    @Autowired
    LevelTopicRepository levelTopicRepository;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    LevelRepository levelRepository;

    @ApiOperation(value = "Get a list of all Level Topics", response = LevelTopic.class, tags = "getAllLevelTopics")
    @GetMapping("/levtops")
    public List<LevelTopic> getAllLevelTopics() {
        return levelTopicRepository.findAll();
    }

    @ApiOperation(value = "Get a list of all Level Topics from School", response = LevelTopic.class, tags = "getAllLevelTopicsFromSchool")
    @GetMapping("/levtops/{schoolId}")
    public List<LevelTopic> getAllLevelTopicsFromSchool(@PathVariable int schoolId) {
        List<LevelTopic> allLevelTopics = levelTopicRepository.findAll();

        // Remove levelTopics not from this school
        for (LevelTopic lt : allLevelTopics) {
            if (lt.getId().getLevelId().getSchoolId() != schoolId) {
                allLevelTopics.remove(lt);
            }
        }

        return allLevelTopics;
    }

    @ApiOperation(value = "Get a list of topics associated with a level (levelNum and schoolId)", response = Topic.class, tags = "getLevelsTopics")
    @GetMapping("/levtops/levels/{schoolId}/{levelNum}")
    public List<Topic> getLevelsTopics(@PathVariable int schoolId, @PathVariable int levelNum) {
        LevelKey levelKey = new LevelKey(levelNum, schoolId);
        List<LevelTopic> levelTopics = levelTopicRepository.findByIdLevelId(levelKey);
        List<Topic> topics = new ArrayList<>();
        for (LevelTopic lt : levelTopics) {
            topics.add(lt.getTopic());
        }
        return topics;
    }

    @ApiOperation(value = "Get a list of levels associated with a topic", response = Levels.class, tags = "getTopicsLevels")
    @GetMapping("/levtops/topics/{tid}")
    public List<Levels> getTopicsLevels(@PathVariable int tid) {
        List<LevelTopic> levelTopics = levelTopicRepository.findByIdTid(tid);
        List<Levels> levels = new ArrayList<>();
        for (LevelTopic lt : levelTopics) {
            levels.add(lt.getLevel());
        }
        return levels;
    }

    @ApiOperation(value = "Add a topic to a level", response = String.class, tags = "addTopicToLevel")
    @PostMapping("/levtops/{levelNum}/{schoolId}/{tid}")
    public ResponseEntity<String> addTopicToLevel(@PathVariable int levelNum, @PathVariable int schoolId, @PathVariable int tid) {
        LevelKey levelKey = new LevelKey(levelNum, schoolId);
        Levels level = levelRepository.findById(levelKey);
        Topic topic = topicRepository.findById(tid);
        
        if (level == null || topic == null) {
            return ResponseEntity.badRequest().body("Object not found in database.");
        }

        LevelTopic levelTopic = new LevelTopic(level, topic);
        levelTopicRepository.save(levelTopic);

        return ResponseEntity.ok("Topic added to level successfully");
    }

}
