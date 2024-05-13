package Backend.ICPC.Repositories;

import Backend.ICPC.Models.Coach;
import Backend.ICPC.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CoachRepository extends JpaRepository<Coach, Long> {
    Coach findById(int id);

    Coach findByEmailAddress(String emailAddress);

    @Transactional
    void deleteById(int id);

    @Query(value = "select * from users where school_id = ?1 AND user_type = 'coach'", nativeQuery = true)
    List<Coach> getCoachesFromSchool(int scid);
}
