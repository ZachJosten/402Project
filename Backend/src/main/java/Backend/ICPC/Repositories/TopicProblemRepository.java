package Backend.ICPC.Repositories;

import Backend.ICPC.Models.Problem;
import Backend.ICPC.Models.TopicsProblems.TopicProblem;
import Backend.ICPC.Models.TopicsProblems.TopicProblemKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicProblemRepository extends JpaRepository<TopicProblem, Long> {
    TopicProblem findById(TopicProblemKey id);

    //@Query(value = "select * from assigned_problems where team_id = ?1 AND problem_id = ?2", nativeQuery = true)
    //List<TopicProblem> findTeamProbs(int tid, int pid);
    //Custom SQL methods to go here.
    //Should probably be things like deleting by team id and problem id.
    //Finding by same parameters would also be good.
}
