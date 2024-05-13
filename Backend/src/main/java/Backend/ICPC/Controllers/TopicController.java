package Backend.ICPC.Controllers;

import Backend.ICPC.Models.School;
import Backend.ICPC.Models.Topics.Topic;
import Backend.ICPC.Models.Topics.TopicKey;
import Backend.ICPC.Repositories.SchoolRepository;
import Backend.ICPC.Repositories.TopicRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@Api(value = "Topic controller", description = "REST API related to Topic Entity", tags = "TopicController")
@RestController
public class TopicController {
    private static final Logger logger = LoggerFactory.getLogger(TopicController.class);

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @ApiOperation(value = "Get a list of all topics", response = Topic.class, tags = "getAllTopics")
    @GetMapping("/topics")
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    @ApiOperation(value = "Get a list of all topics from School", response = Topic.class, tags = "getAllTopicsFromSchool")
    @GetMapping("/topics/school/{sid}")
    public List<Topic> getTopicsBySchool(@PathVariable int sid) {
        List<Topic> topics = topicRepository.findBySchool_Id(sid);
        
        return topics;
    }

    @ApiOperation(value = "Add a new topic", response = ResponseEntity.class, tags = "addTopic")
    @PostMapping(path = {"/topics"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> addTopic(@RequestBody HashMap<String, Object> request) {
        logger.info("Entering addTopic method");

        String name = (String) request.get("name");
        int schoolId = (int) request.get("schoolId");
        logger.info("Received request to add topic with name: {} and schoolId: {}", name, schoolId);

        School school = schoolRepository.findById(schoolId);
        if (school == null) {
            return ResponseEntity.badRequest().body("No school found with id: " + schoolId);
        }

        Topic topic = new Topic(name, school);
        topicRepository.save(topic);
        logger.info("Topic added successfully with name: {}", name);

        return ResponseEntity.ok("Added new topic.");
    }
}
