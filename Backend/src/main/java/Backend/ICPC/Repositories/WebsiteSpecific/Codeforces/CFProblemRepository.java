package Backend.ICPC.Repositories.WebsiteSpecific.Codeforces;

import Backend.ICPC.Models.WebsiteSpecific.Codeforces.CodeforcesProblem;
import Backend.ICPC.Repositories.ProblemRepository;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CFProblemRepository extends ProblemRepository<CodeforcesProblem> {
    @Query(value = "select * from problems where con_id = ?1 AND ind = ?2", nativeQuery = true)
    List<CodeforcesProblem> findCurrentExisting(int cond, String inde);

}
