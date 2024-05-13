package Backend.ICPC.Repositories;

import Backend.ICPC.Models.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SchoolRepository extends JpaRepository<School, Long> {
    School findById(int id);

    @Query(value = "select * from school where name = ?1", nativeQuery = true)
    School findSchoolByName(String name);
}
