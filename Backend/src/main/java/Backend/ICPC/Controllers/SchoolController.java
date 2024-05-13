package Backend.ICPC.Controllers;

import Backend.ICPC.Models.School;
import Backend.ICPC.Models.Team;
import Backend.ICPC.Models.Levels.Levels;
import Backend.ICPC.Repositories.LevelRepository;
import Backend.ICPC.Repositories.SchoolRepository;
import Backend.ICPC.Repositories.TeamRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

@Api(value="School Controller", description="REST API related to School Entity",tags="SchoolController")
@RestController
public class SchoolController {
    @Autowired
    SchoolRepository schoolRepository;

    @ApiOperation(value = "Get a list of all Schools", response = School.class, tags = "getAllSchools")
    @GetMapping(path = {"/schools"})
    List<School> getAllSchools() { return this.schoolRepository.findAll(); }

    @ApiOperation(value = "Get a School by id", response = School.class, tags = "getSchoolByID")
    @GetMapping(path = {"/schools/{sid}"})
    School getSchoolByID(@PathVariable int sid)
    {
        return this.schoolRepository.findById(sid);
    }

    @ApiOperation(value = "Get a school by name", response = School.class, tags = "getSchoolByName")
    @GetMapping(path = {"/schools/name"})
    School getSchoolByName(@Parameter(name = "request", example = "{\n name : Iowa State University \n}") @RequestBody HashMap<String, Object> request)
    {
        String name = (String)(request.get("name"));
        School school = schoolRepository.findSchoolByName(name);

        return school;
    }

    @ApiOperation(value = "Create and save a new School in the database", response = ResponseEntity.class, tags = "createSchool")
    @PostMapping(path = {"/schools"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createSchool(@Parameter(name = "request", example = "{\n name : Iowa State University \n}") @RequestBody HashMap<String, Object> request) {
        // Extract fields from the request map
        String name = (String) request.get("name");
        
        School school = new School(name);
        schoolRepository.save(school);

        return ResponseEntity.ok("School created successfully");
    }

}
