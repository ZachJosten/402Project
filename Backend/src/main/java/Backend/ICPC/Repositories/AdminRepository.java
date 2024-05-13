package Backend.ICPC.Repositories;

import Backend.ICPC.Models.Admin;
import Backend.ICPC.Models.Coach;
import Backend.ICPC.Models.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findById(int id);

    Admin findByEmailAddress(String emailAddress);

    @Transactional
    void deleteById(int id);

    @Query(value = "select * from users where school_id = ?1 AND user_type = 'admin'", nativeQuery = true)
    List<Admin> getAdminsFromSchool(int scid);
}
