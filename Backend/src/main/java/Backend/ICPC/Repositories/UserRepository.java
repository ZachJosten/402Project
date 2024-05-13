package Backend.ICPC.Repositories;

import Backend.ICPC.Models.Student;
import Backend.ICPC.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);

    User findByEmailAddress(String emailAddress);

    @Transactional
    void deleteById(int id);

    @Query(value = "select user_type from users where id = ?1", nativeQuery = true)
    String getUserType(int id);
}
