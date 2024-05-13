package Backend.ICPC.Controllers;

import Backend.ICPC.Models.*;
import Backend.ICPC.Repositories.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

@Api(value="Team Controller", description="REST API related to Team Entity",tags="TeamController")
@RestController
public class TeamController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    
    @Autowired
    TeamRepository teamRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CoachRepository coachRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @ApiOperation(value = "Get a list of all Teams", response = Team.class, tags = "getAllTeams")
    @GetMapping(path = {"/teams"})
    List<Team> getAllTeams() { return this.teamRepository.findAll(); }

    @ApiOperation(value = "Find a team by tid", response = Team.class, tags = "getTeamByTID")
    @GetMapping(path = {"/teams/{tid}"})
    Team getTeamByTID(@PathVariable int tid) { return this.teamRepository.findByTeamId(tid); }

    @ApiOperation(value = "Create and save a new Team in the database", response = ResponseEntity.class, tags = "createTeam")
    @PostMapping(path = {"/teams"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createTeam(@Parameter(name = "request", example = "{\n coach_id : 3, \n school_id : 1, \n student_ids : [1, 2, 4, 5] \n}") @RequestBody HashMap<String, Object> request) {
        int cid = (int) request.get("coach_id");
        // int school_id = (int) request.get("school_id");
        @SuppressWarnings("unchecked")
        List<Integer> sids = (List<Integer>) request.get("student_ids");
        
        // // Get school from school_id
        // School school = schoolRepository.findBySchoolId(school_id);
        // if (school == null) { return ResponseEntity.badRequest().body("There does not exist a school with id " + school_id); }

        // Get coach from coach_id
        Coach coach = coachRepository.findById(cid);
        if (coach == null) { return ResponseEntity.badRequest().body("There does not exist a coach with id " + cid); }
        
        // Get students from student_ids
        List<Student> students = new ArrayList<>();
        for (Integer sid : sids) {
            Student student = studentRepository.findBySid(sid);
            if (student == null) { return ResponseEntity.badRequest().body("There does not exist a student with id " + sid); }
            students.add(student);
        }

        Team team = new Team(coach, students);
        
        // Update coach and students with new Team
        coach.addTeam(team);
        for (Student s : team.getStudents()){
            s.setTeam(team);
        }

        // Save in database
        teamRepository.save(team);

        return ResponseEntity.ok("Team created successfully");
    }

    @ApiOperation(value = "Add student to team roster via id", response = ResponseEntity.class, tags = "addStudent")
    @PostMapping(path={"/teams/students/{tid}/{sid}"})
    ResponseEntity<String> addStudent(@PathVariable int tid, @PathVariable int sid)
    {
        Student student = this.studentRepository.findById(sid);
        if (student == null) { return ResponseEntity.badRequest().body("There does not exist a student with id " + sid); }
        Team team = this.teamRepository.findByTeamId(tid);
        student.setTeam(team);

        // Check that this student is not already on this Team's list of Students
        if (!team.getStudents().contains(student)) {
            team.addStudent(student);
        }

        // Save to database
        studentRepository.save(student);
        
        return ResponseEntity.ok("Student added successfully");
    }

    @ApiOperation(value = "Get the students of a team", response = Student.class, tags = "getStudents")
    @GetMapping(path={"/teams/students/{tid}"})
    List<Student> getStudents(@PathVariable int tid)
    {
        return this.teamRepository.findByTeamId(tid).getStudents();
    }

    @ApiOperation(value = "Get the coach of a team", response = Coach.class, tags = "getCoach")
    @GetMapping(path={"/teams/coach/{tid}"})
    Coach getCoach(@PathVariable int tid)
    {
        return this.teamRepository.findByTeamId(tid).getCoach();
    }

    @ApiOperation(value = "Delete a team by id", response = ResponseEntity.class, tags = "deleteTeam")
    @DeleteMapping("/teams/{tid}")
    @Transactional
    public ResponseEntity<String> deleteTeamById(@PathVariable int tid) {
        try {
            this.teamRepository.deleteByTeamId(tid);
            logger.info("Team deleted successfully, ID: " + tid);
            return ResponseEntity.ok("Team deleted successfully");
        } catch (Exception e) {
            logger.error("Failed to delete team with ID " + tid, e); // Make sure you have a Logger set up.
            return ResponseEntity.badRequest().body("Error deleting team with ID: " + tid);
        }
    }

}
