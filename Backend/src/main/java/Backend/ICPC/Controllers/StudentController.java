package Backend.ICPC.Controllers;

import Backend.ICPC.Models.*;
import Backend.ICPC.Models.Levels.LevelKey;
import Backend.ICPC.Models.Levels.Levels;
import Backend.ICPC.Repositories.*;
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

import java.util.HashMap;
import java.util.List;

@Api(value="Student Controller", description="REST API related to Student Entity",tags="StudentController")
@RestController
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    LevelRepository levelRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @ApiOperation(value = "Get a list of all Students", response = Student.class, tags = "getAllStudents")
    @GetMapping(path = {"/students"})
    List<Student> getAllStudents() { return this.studentRepository.findAll(); }

    @ApiOperation(value = "Get a student by id", response = Student.class, tags = "getStudentByID")
    @GetMapping(path = {"/students/id/{id}"})
    Student getStudentByID(@PathVariable int id)
    {
        return this.studentRepository.findById(id);
    }

    @ApiOperation(value = "Get a student by email address", response = Student.class, tags = "getStudentByEmail")
    @GetMapping(path = {"/students/email/{email}"})
    Student getStudentByEmail(@PathVariable String email)
    {
        return this.studentRepository.findByEmailAddress(email);
    }

    @ApiOperation(value = "Get the students of specific school", response = Student.class, tags = "getStudentsBySchool")
    @GetMapping(path = {"/students/{scid}"})
    List<Student> getStudentsBySchool(@PathVariable int scid)
    {
        return studentRepository.getStudentsFromSchool(scid);
    }

    @ApiOperation(value = "Create and save a new Student in the database", response = ResponseEntity.class, tags = "createStudent")
    @PostMapping(path = {"/students"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createStudent(@Parameter(name = "request", example = "{\n name : John Doe, \n emailAddress : jdoe@gmail.com, \n password : 1234, \n student_id : 302, \nschool_id : 1 \n}") @RequestBody HashMap<String, Object> request) {
        // Extract fields from the request map
        String name = (String) request.get("name");
        String email = (String) request.get("emailAddress");
        String pwd = (String) request.get("password");
        int sid = (int) request.get("student_id");
        int school_id = (int) request.get("school_id");
        
        // Get school from school_id
        School school = schoolRepository.findById(school_id);
        if (school == null) { return ResponseEntity.badRequest().body("There does not exist a school with id " + school_id); }

        // Hash password
        pwd = PasswordUtils.hashPassword(pwd);

        // Student not assigned team on creation
        if (request.get("team_id") == null) {
            Student student = new Student(name, email, pwd, sid, null, school);

            studentRepository.save(student);
        } 
        // Student assigned team on creation
        else {
            int tid = (int) request.get("team_id");
            Team team = teamRepository.findByTeamId(tid);
            if (team == null) { return ResponseEntity.badRequest().body("There does not exist a team with id " + tid); }
        
            Student student = new Student(name, email, pwd, sid, team, school);
            team.addStudent(student);

            studentRepository.save(student);
        }

        return ResponseEntity.ok("Student created successfully");
    }

    @ApiOperation(value = "Get student (by id) level", response = Levels.class, tags = "getStudentLevel")
    @GetMapping(path = {"/students/level/{id}"})
    Levels getStudentLevel(@PathVariable int id)
    {
        return studentRepository.findById(id).getLev();
    }

    @ApiOperation(value = "Add a student (by id) to a level", response = ResponseEntity.class, tags = "addStudentLevel")
    @PostMapping(path = {"/students/level/{id}/{levelNum}/{schoolId}"})
    ResponseEntity<String> addStudentLevel(@PathVariable int id, @PathVariable int levelNum, @PathVariable int schoolId)
    {
        Student student = studentRepository.findById(id);
        if (student == null) { return ResponseEntity.badRequest().body("There does not exist a student with id: " + id); }

        // Find level from levelNum, schoolId
        LevelKey key = new LevelKey(levelNum, schoolId);
        Levels level = levelRepository.findById(key);
        if (level == null) { return ResponseEntity.badRequest().body("There does not exist a level with key levelNum: " + levelNum + ", schoolId: " + schoolId); }

        student.setLev(level);
        studentRepository.save(student);

        return ResponseEntity.ok("Student assigned to level.");
    }

    @ApiOperation(value = "Get school of student", response = School.class, tags="getSchool")
    @GetMapping(path = {"/students/school/{id}"})
    School getSchool(@PathVariable int id)
    {
        Student student = studentRepository.findById(id);
        return student.getSchool();
    }

    @ApiOperation(value = "Get list of student's submissions", response = Submission.class, tags = "getStudentSubmissions")
    @GetMapping(path = {"/students/submissions/{sid}"})
    List<Submission> getStudentSubmissions(@PathVariable int sid)
    {
        return studentRepository.findById(sid).getSubmissionList();
    }

    @ApiOperation(value = "Delete a student by id", response = ResponseEntity.class, tags = "deleteStudent")
    @DeleteMapping("/students/{id}")
    @Transactional
    public ResponseEntity<String> deleteStudentById(@PathVariable int id) {
        try {
            this.studentRepository.deleteById(id);
            logger.info("Student deleted successfully, ID: " + id);
            return ResponseEntity.ok("Student deleted successfully");
        } catch (Exception e) {
            logger.error("Failed to delete student with ID " + id, e); // Make sure you have a Logger set up.
            return ResponseEntity.badRequest().body("Error deleting student with ID: " + id);
        }
    }

}
