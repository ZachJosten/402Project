package Backend.ICPC.Repositories;

import Backend.ICPC.Models.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findById(int id);

    Student findBySid(int sid);

    Student findByEmailAddress(String emailAddress);

    @Transactional
    void deleteBySid(int sid);

    @Transactional
    void deleteById(int id);

    @Query(value = "select * from users where school_id = ?1 AND user_type = 'student'", nativeQuery = true)
    List<Student> getStudentsFromSchool(int scid);
}
