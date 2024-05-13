package Backend.ICPC.Controllers;

import Backend.ICPC.Models.*;
import Backend.ICPC.Models.Levels.LevelKey;
import Backend.ICPC.Models.Levels.Levels;
import Backend.ICPC.Repositories.LevelRepository;
import Backend.ICPC.Repositories.SchoolRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Api(value = "Level controller", description = "REST API related to Level Entity", tags = "LevelController")
@RestController
public class LevelController {

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @ApiOperation(value = "Get a list of all Levels", response = Levels.class, tags = "getAllLevels")
    @GetMapping("/levels")
    public List<Levels> getAllLevels() {
        return levelRepository.findAll();
    }

    @ApiOperation(value = "Add a new level", response = ResponseEntity.class, tags = "addLevel")
    @PostMapping("/levels")
    public ResponseEntity<String> addLevel(@RequestBody HashMap<String, Object> request) {
        int level_num = (int) request.get("level_num");
        String descrip = (String) request.get("descrip");
        int schoolId = (int) request.get("school_id");

        // Find school from schoolId
        School school = schoolRepository.findById(schoolId);
        if (school == null) {
            return ResponseEntity.badRequest().body("There does not exist a school with id " + schoolId);
        }

        Levels newLevel = new Levels(level_num, descrip, school);
        levelRepository.save(newLevel);

        return ResponseEntity.ok("New level created");
    }

    @ApiOperation(value = "Get a level (levelNum and schoolId)", response = Levels.class, tags = "getLevelBySchool")
    @GetMapping("/levels/{levelNum}/{schoolId}")
    public ResponseEntity<Object> getLevelByLevelNumAndSchoolId(@PathVariable int levelNum, @PathVariable int schoolId) {
        // Create key to search repository
        LevelKey key = new LevelKey(levelNum, schoolId);
        
        // Find level from key
        Levels level = levelRepository.findById(key);
        if (level == null) { return ResponseEntity.badRequest().body("There does not exist a level with key " + levelNum + " " + schoolId); }

        return ResponseEntity.ok(level);
    }

    @ApiOperation(value = "Get a list of all levels from a school", response = Levels.class, tags = "getLevelsBySchool")
    @GetMapping("/levels/schools/{schoolId}")
    public List<Levels> getLevelsBySchool(@PathVariable int schoolId) {
        return levelRepository.findByIdSchoolId(schoolId);
    }

    @ApiOperation(value = "Get list of students in a level (levelNum and schoolId)", response = Student.class, tags = "getLevelsStudents")
    @GetMapping("/levels/students/{levelNum}/{schoolId}")
    public ResponseEntity<Object> getLevelsStudents(@PathVariable int levelNum, @PathVariable int schoolId) {
        // Create key to search repository
        LevelKey key = new LevelKey(levelNum, schoolId);
        
        Levels level = levelRepository.findById(key);
        if (level == null) { return ResponseEntity.badRequest().body("There does not exist a level with key " + levelNum + " " + schoolId); }

        return ResponseEntity.ok(level.getStudents());
    }
}
