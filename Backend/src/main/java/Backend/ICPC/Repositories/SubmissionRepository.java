package Backend.ICPC.Repositories;

import Backend.ICPC.Models.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Submission findById(int id);

    @Query(value = "select * from submission where sub_pid = ?1 AND sub_sid = ?2 AND submission_id = ?3", nativeQuery = true)
    List<Submission> findCurrentExisting(int pid, int sid, int subid);

    @Query(value = "select COUNT(*) from submission where sub_sid = ?1 AND (verdict = 'OK' OR verdict = 'Accepted')", nativeQuery = true)
    double getAcceptedAmount(int stid);

    @Query(value = "select COUNT(*) from submission where sub_sid = ?1", nativeQuery = true)
    double getTotalStudentSubmissions(int stid);

    @Query(value = "select * from submission as sub INNER JOIN problems as prob on sub.sub_pid = prob.id INNER JOIN topic_problem as tp on prob.id = tp.problem_id INNER JOIN topic as top on tp.topic_id = top.id WHERE (sub.sub_sid = ?1 AND top.name = ?2)", nativeQuery = true)
    List<Submission> getTopicsSubmissions(int stid, String topName);

    @Query(value = "select COUNT(*) from submission as sub INNER JOIN problems as prob on sub.sub_pid = prob.id INNER JOIN topic_problem as tp on prob.id = tp.problem_id INNER JOIN topic as top on tp.topic_id = top.id WHERE (sub.sub_sid = ?1 AND top.name = ?2)", nativeQuery = true)
    double countTopicsSubmissions(int stid, String topName);
}
