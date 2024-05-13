package Backend.ICPC.Controllers;

import Backend.ICPC.Models.Student;
import Backend.ICPC.Models.User;
import Backend.ICPC.Repositories.UserRepository;
import Backend.ICPC.Security.PasswordUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Api(value="User Controller", description="REST API related to User Entity",tags="UserController")
@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String index() {
        return "Hello, from Springboot controller";
    }

    @ApiOperation(value = "Get a list of all Users", response = User.class, tags = "getAllUsers")
    @GetMapping(path = {"/users"})
    List<User> getAllUsers() { return this.userRepository.findAll(); }

    @ApiOperation(value = "Get a user by id", response = User.class, tags = "getUserById")
    @GetMapping(path = {"/users/id/{id}"})
    User getUserById(@PathVariable int id)
    {
        return this.userRepository.findById(id);
    }

    @ApiOperation(value = "Get a user by email address", response = User.class, tags = "getUserByEmail")
    @GetMapping(path = {"/users/email/{email}"})
    User getUserByEmail(@PathVariable String email)
    {
        return this.userRepository.findByEmailAddress(email);
    }

    @ApiOperation(value = "Get user type of a user", response = String.class, tags = "getUserTypeById")
    @GetMapping(path={"/userType/{uid}"})
    String getUserTypeById(@PathVariable int uid)
    {
        return this.userRepository.getUserType(uid);
    }

    @ApiOperation(value = "Authenticate user", response = ResponseEntity.class, tags = "authenticateUser")
    @PostMapping(path = {"/users/authenticate"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createStudent(@Parameter(name = "request", example = "{\n emailAddress : jdoe@gmail.com, \n password : 1234 \n}") @RequestBody HashMap<String, Object> request) {
        // Extract fields from the request map
        String email = (String) request.get("emailAddress");
        String pwd = (String) request.get("password");

        User user = this.userRepository.findByEmailAddress(email);
        if (user == null) { return ResponseEntity.badRequest().body("Couldn't find user with email address " + email); }

        // Check user's password
        if (!PasswordUtils.checkPassword(pwd, user.getPassword())) { 
            return ResponseEntity.badRequest().body("Incorrect password");
        }

        return ResponseEntity.ok("Successfully logged in user");
    }
}
