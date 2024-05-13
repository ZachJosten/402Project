package Backend.ICPC.Controllers;

import Backend.ICPC.Models.Problem;
import Backend.ICPC.Models.Topics.Topic;
import Backend.ICPC.Models.TopicsProblems.TopicProblem;
import Backend.ICPC.Repositories.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value = "TopicProblem controller", description="REST API related to Topic Problem Entity", tags="TopicProblemController")
@RestController
public class TopicProblemController {

    @Autowired
    TopicProblemRepository tPR;

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    StudentRepository studentRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Get a list of all Topic Problems", response = TopicProblem.class, tags = "getAllTopicProblems")
    @GetMapping(path = {"/toprobs"})
    List<TopicProblem> getAllTopicProblems() { return this.tPR.findAll(); }

    @ApiOperation(value = "Get a list of all Topic Problems from School", response = TopicProblem.class, tags = "getAllTopicProblemsFromSchool")
    @GetMapping(path = {"/toprobs/{schoolId}"})
    List<TopicProblem> getAllTopicProblemsFromSchool(@PathVariable int schoolId) { 
        List<TopicProblem> all_topicProblems = this.tPR.findAll();
        
        // Extract topicProblems only from this school
        List<TopicProblem> topicProblemsFromThisSchool = new ArrayList<TopicProblem>();
        for (TopicProblem topicProblem : all_topicProblems) {
            if (topicProblem.getTopic().getSchool().getId() == schoolId) {
                topicProblemsFromThisSchool.add(topicProblem);
            }
        }

        return topicProblemsFromThisSchool;
    }

    @ApiOperation(value = "Add a topic to a problem", response = ResponseEntity.class, tags = "addProblemTopic")
    @PostMapping(path = {"/toprobs/{pid}/{tid}"})
    ResponseEntity<String> addProblemTopic(@PathVariable int pid, @PathVariable int tid)
    {
        Problem problem = problemRepository.findById(pid);
        Topic topic = topicRepository.findById(tid);
        if(problem == null || topic == null)
        {
            return new ResponseEntity<String>("No such object in database", HttpStatus.BAD_REQUEST);
        }
        TopicProblem topicProblem = new TopicProblem(topic, problem);
        tPR.save(topicProblem);
        return ResponseEntity.ok("Topic added to problem.");
    }

    @ApiOperation(value = "Get problems of a certain topic", response = Problem.class, tags = "getTopicsProblems")
    @GetMapping(path = {"/toprobs/topics/{tid}"})
    List<Problem> getTopicsProblems(@PathVariable int tid)
    {
        List<Problem> problems = new ArrayList<>();
        List<TopicProblem> tps = topicRepository.findById(tid).getTopicProblems();
        for(TopicProblem tp : tps)
        {
            problems.add(tp.getProblem());
        }

        return problems;
    }

    @ApiOperation(value = "Get topics of a problem (from School)", response = Problem.class, tags = "getProblemsTopics")
    @GetMapping(path = {"/toprobs/problems/{pid}/{sid}"})
    List<Topic> getProblemsTopics(@PathVariable int pid, @PathVariable int sid)
    {
        List<Topic> topics = new ArrayList<>();
        List<TopicProblem> tps = problemRepository.findById(pid).getTopicProblems();
        for(TopicProblem tp : tps)
        {
            Topic t = tp.getTopic();
            if (t.getSchool().getId() == sid) {
                topics.add(t);
            }
        }

        return topics;
    }

    /*

    @ApiOperation(value = "Get a list of all assigned problems", response= Topic.class, tags="getAllAssignedProblems")
    @GetMapping(path={"/aprobs"})
    public List<Topic> getAllAssignedProblems() { return this.aPR.findAll(); }

    @ApiOperation(value = "Get list of problems in team repo via coach id", response= Topic.class, tags="getCoachesTeamProblems")
    @GetMapping(path="/aprobs/{cid}")
    public List<Topic> getCoachesTeamProblems(@PathVariable int cid)
    {
        Team team = coachRepository.findById(cid).getTeam();
        return team.getAssignedProblems();
    }

    @ApiOperation(value = "Add a problem into a teams assigned problems", response=String.class, tags="addOrRemToTeamRepo")
    @PostMapping(path={"/addOrRemToRepo/{cid}/{pid}"})
    public String addOrRemToTeamRepo(@PathVariable int cid, @PathVariable int pid)
    {
        Team team = coachRepository.findById(cid).getTeam();
        Problem prob = problemRepository.findById(pid);
        List<Topic> curProb = aPR.findTeamProbs(team.getTeamId(), pid);

        if(curProb.size() > 0)
        {
            for(Topic ap : curProb)
            {
                aPR.delete(ap);
            }
            return success;
        }

        List<Student> stuList = team.getStudents();
        for(Student s : stuList)
        {
            Topic ap = new Topic(team, prob, s ,"");
            //prob.addASP(ap);
            aPR.save(ap);
        }
        //For all students in team, add the assigned problem to them, with it not being visible.
        //team.addASP(ap);
        //problemRepository.save(prob);


        return success;
    }

    @ApiOperation(value = "Assign a problem in team's repo to student", response = String.class, tags="assignToStudent")
    @PostMapping(path={"/assignToStudent/{tid}/{pid}/{sid}"})
    public String assignToStudent(@PathVariable int tid, @PathVariable int pid, @PathVariable int sid)
    {
        TopicKey apk = new TopicKey(tid, pid, sid);
        Topic ap = this.aPR.findById(apk);
        ap.setVisible(true);
        aPR.save(ap);

        return success;
    }
     */

}
