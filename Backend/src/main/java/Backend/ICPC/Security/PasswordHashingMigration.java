// package Backend.ICPC.Security;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.stereotype.Component;

// import Backend.ICPC.Models.User;
// import Backend.ICPC.Repositories.UserRepository;

// import javax.annotation.PostConstruct;
// import java.util.List;

// @Component
// public class PasswordHashingMigration {

//     @Autowired
//     private UserRepository userRepository;

//     @PostConstruct
//     public void hashExistingPasswords() {
//         BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//         List<User> users = userRepository.findAll();
//         for (User user : users) {
//             String currentPassword = user.getPassword();
//             if (!isHashed(currentPassword)) {
//                 String hashedPassword = encoder.encode(currentPassword);
//                 user.setPassword(hashedPassword);
//                 userRepository.save(user);
//             }
//         }
//     }

//     private boolean isHashed(String password) {
//         // Check if the password is already hashed using a pattern or length check
//         return password != null && password.matches("^\\$2[ayb]\\$.{56}$");
//     }
// }

