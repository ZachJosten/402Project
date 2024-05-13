package Backend.ICPC.Controllers;

import Backend.ICPC.Models.Admin;
import Backend.ICPC.Models.Coach;
import Backend.ICPC.Models.School;
import Backend.ICPC.Repositories.AdminRepository;
import Backend.ICPC.Repositories.SchoolRepository;
import Backend.ICPC.Security.PasswordUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.HashMap;

@Api(value="Admin Controller", description="REST API related to Admin Entity",tags="AdminController")
@RestController
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @ApiOperation(value = "Get a list of all Admins", response = Admin.class, tags = "getAllAdmins")
    @GetMapping(path = {"/admins"})
    List<Admin> getAllAdmins() { return this.adminRepository.findAll(); }

    @ApiOperation(value = "Find an admin by id", response = Admin.class, tags = "getAdminByID")
    @GetMapping(path = {"/admins/id/{id}"})
    Admin getAdminByID(@PathVariable int id)
    {
        return this.adminRepository.findById(id);
    }

    @ApiOperation(value = "Find an admin by email", response = Admin.class, tags = "getAdminByEmail")
    @GetMapping(path = {"/admins/email/{email}"})
    Admin getAdminByEmail(@PathVariable String email)
    {
        return this.adminRepository.findByEmailAddress(email);
    }

    @ApiOperation(value = "Get the admin(s) of specific school", response = Admin.class, tags = "getAdminsBySchool")
    @GetMapping(path = {"/admins/{scid}"})
    List<Admin> getAdminsBySchool(@PathVariable int scid)
    {
        return adminRepository.getAdminsFromSchool(scid);
    }

    @ApiOperation(value = "Create and save a new Admin in the database", response = ResponseEntity.class, tags = "createAdmin")
    @PostMapping(path = {"/admins"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createAdmin(@Parameter(name = "request", example = "{\n name : John Doe, \n emailAddress : jdoe@gmail.com, \n password : 1234, \n school_id : 1 \n}") @RequestBody HashMap<String, Object> request) {
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

        // Create Admin (requires school)
        Admin myAdmin = new Admin(name, email, pwd, school);
        adminRepository.save(myAdmin);

        return ResponseEntity.ok("Admin created successfully");
    }

    @ApiOperation(value = "Get school of Admin", response = School.class, tags="getSchool")
    @GetMapping(path = {"/admins/school/{id}"})
    School getSchool(@PathVariable int id)
    {
        Admin admin = this.adminRepository.findById(id);
        return admin.getSchool();
    }

    @ApiOperation(value = "Delete an admin by id", response = ResponseEntity.class, tags = "deleteAdmin")
    @DeleteMapping("/admins/{id}")
    @Transactional
    public ResponseEntity<String> deleteAdminById(@PathVariable int id) {
        try {
            this.adminRepository.deleteById(id);
            logger.info("Admin deleted successfully, ID: " + id);
            return ResponseEntity.ok("Admin deleted successfully");
        } catch (Exception e) {
            logger.error("Failed to delete admin with ID " + id, e); // Make sure you have a Logger set up.
            return ResponseEntity.badRequest().body("Error deleting admin with ID: " + id);
        }
    }

}
