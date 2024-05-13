package Backend.ICPC.Repositories.WebsiteSpecific.uHunt;

import Backend.ICPC.Models.WebsiteSpecific.uHunt.uHuntProblem;
import Backend.ICPC.Repositories.ProblemRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface uHuntProblemRepository extends ProblemRepository<uHuntProblem> {
    @Query(value = "select * from problems where uid = ?1", nativeQuery = true)
    List<uHuntProblem> findCurrentExisting(int pid);
}
