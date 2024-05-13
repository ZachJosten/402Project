package Backend.ICPC.Controllers;

import Backend.ICPC.Models.Coach;
import Backend.ICPC.Models.School;
import Backend.ICPC.Models.Student;
import Backend.ICPC.Models.Team;
import Backend.ICPC.Repositories.CoachRepository;
import Backend.ICPC.Repositories.SchoolRepository;
import Backend.ICPC.Repositories.TeamRepository;
import Backend.ICPC.Security.PasswordUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.HashMap;

@Api(value="Coach Controller", description="REST API related to Coach Entity",tags="CoachController")
@RestController
public class CoachController {
    private static final Logger logger = LoggerFactory.getLogger(CoachController.class);

    @Autowired
    CoachRepository coachRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    TeamRepository teamRepository;

    @ApiOperation(value = "Get a list of all Coaches", response = Coach.class, tags = "getAllCoaches")
    @GetMapping(path = {"/coaches"})
    List<Coach> getAllCoaches() { return this.coachRepository.findAll(); }

    @ApiOperation(value = "Find a coach by id", response = Coach.class, tags = "getCoachByID")
    @GetMapping(path = {"/coaches/id/{cid}"})
    Coach getCoachByID(@PathVariable int cid)
    {
        return this.coachRepository.findById(cid);
    }

    @ApiOperation(value = "Get a coach by email address", response = Coach.class, tags = "getCoachByEmail")
    @GetMapping(path = {"/coaches/email/{email}"})
    Coach getCoachByEmail(@PathVariable String email)
    {
        return this.coachRepository.findByEmailAddress(email);
    }

    @ApiOperation(value = "Get the coaches of specific school", response = Coach.class, tags = "getCoachesBySchool")
    @GetMapping(path = {"/coaches/{scid}"})
    List<Coach> getCoachesBySchool(@PathVariable int scid)
    {
        return coachRepository.getCoachesFromSchool(scid);
    }

    @ApiOperation(value = "Create and save a new Coach in the database", response = ResponseEntity.class, tags = "createCoach")
    @PostMapping(path = {"/coaches"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createCoach(@Parameter(name = "request", example = "{\n name : John Doe, \n emailAddress : jdoe@gmail.com, \n password : 1234, \n school_id : 1 \n}") @RequestBody HashMap<String, Object> request) {
        // Extract fields from the request map
        String name = (String) request.get("name");
        String email = (String) request.get("emailAddress");
        String pwd = (String) request.get("password");
        int school_id = (int) request.get("school_id");

        // Hash password
        pwd = PasswordUtils.hashPassword(pwd);

        // Get school from school_id
        School school = schoolRepository.findById(school_id);
        if (school == null) { return ResponseEntity.badRequest().body("There does not exist a school with id " + school_id); }

        // Create coach
        Coach myCoach = new Coach(name, email, pwd, school);
        coachRepository.save(myCoach);

        return ResponseEntity.ok("Coach created successfully");
    }

    @ApiOperation(value = "Get teams of coach", response = Team.class, tags="getTeams")
    @GetMapping(path = {"/coaches/teams/{cid}"})
    List<Team> getTeam(@PathVariable int cid)
    {
        Coach coach = this.coachRepository.findById(cid);
        return coach.getTeams();
    }

    @ApiOperation(value = "Get school of coach", response = School.class, tags="getSchool")
    @GetMapping(path = {"/coaches/school/{cid}"})
    School getSchool(@PathVariable int cid)
    {
        Coach coach = this.coachRepository.findById(cid);
        return coach.getSchool();
    }

    @ApiOperation(value = "Add a team for a coach", response = ResponseEntity.class, tags = "addTeam")
    @GetMapping(path={"/coaches/teams/{cid}/{tid}"})
    ResponseEntity<String> addTeam(@PathVariable int cid, @PathVariable int tid)
    {
        // Get coach from cid
        Coach coach = coachRepository.findById(cid);
        if (coach == null) { return ResponseEntity.badRequest().body("There does not exist a coach with id " + cid); }
        
        // Get team from tid
        Team team = this.teamRepository.findByTeamId(tid);
        if (team == null) { return ResponseEntity.badRequest().body("There does not exist a team with id " + tid); }
        
        // Update team and coach
        team.setCoach(coach);
        if (!coach.getTeams().contains(team)) {
            coach.addTeam(team);
        }

        // Save to database
        teamRepository.save(team);
        coachRepository.save(coach);

        return ResponseEntity.ok("Team added for Coach.");
    }

    @ApiOperation(value = "Delete a coach by id", response = ResponseEntity.class, tags = "deleteCoach")
    @DeleteMapping("/coaches/{id}")
    @Transactional
    public ResponseEntity<String> deleteCoachById(@PathVariable int id) {
        try {
            this.coachRepository.deleteById(id);
            logger.info("Coach deleted successfully, ID: " + id);
            return ResponseEntity.ok("Coach deleted successfully");
        } catch (Exception e) {
            logger.error("Failed to delete coach with ID " + id, e); // Make sure you have a Logger set up.
            return ResponseEntity.badRequest().body("Error deleting coach with ID: " + id);
        }
    }

}
