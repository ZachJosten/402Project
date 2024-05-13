package Backend.ICPC.Repositories.WebsiteSpecific.Kattis;

import Backend.ICPC.Models.WebsiteSpecific.Kattis.KattisProblem;
import Backend.ICPC.Models.WebsiteSpecific.uHunt.uHuntProblem;
import Backend.ICPC.Repositories.ProblemRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KattisRepository extends ProblemRepository<KattisProblem> {

    @Query(value = "select * from problems where kat_id = ?1", nativeQuery = true)
    List<KattisProblem> findCurrentExisting(int kid);
}
