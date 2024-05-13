package Backend.ICPC.Controllers;

import Backend.ICPC.Models.Problem;
import Backend.ICPC.Models.Student;
import Backend.ICPC.Models.Submission;
import Backend.ICPC.Models.Topics.Topic;
import Backend.ICPC.Models.TopicsProblems.TopicProblem;
import Backend.ICPC.Repositories.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Api(value = "Statistics Controller", description = "Retrieves statistics and returns either whole numbers of a decimal(<= 1)", tags = "StatisticsController")
@RestController
public class StatisticsController {

    @Autowired
    SubmissionRepository submissionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TopicRepository topicRepository;


    @ApiOperation(value = "Get the percent of correctly completed submissions of a user", response = Double.class, tags = "getCorrectPercent")
    @GetMapping(path = {"/stats/{stid}"})
    double getCorrectPercent(@PathVariable int stid)
    {
        double correct = submissionRepository.getAcceptedAmount(stid);
        double total = submissionRepository.getTotalStudentSubmissions(stid);
        return correct / total;
    }

    @ApiOperation(value = "Get list of number of correct and total submissions of certain topic within last specified interval of time for particular student.", response = Double.class, tags = "getCorrectInInterval")
    @PutMapping(path = {"/stats/intev/{stid}"})
    List<Double> getCorrectInInterval(@Parameter(name = "request", example = "{\n count : 20, \n interval : Years, \n topName : Data Structures(or All) \n}") @RequestBody HashMap<String, Object> request, @PathVariable int stid) {
        int count = (int)(request.get("count"));
        String interval = (String) (request.get("interval"));
        String topName = (String) (request.get("topic"));
        Student student = studentRepository.findById(stid);
        List<Submission> submissions;
        if (topName.equals("All")) {
            submissions = student.getSubmissionList();
        } else {
            submissions = submissionRepository.getTopicsSubmissions(student.getId(), topName);
        }

        if (submissions.size() == 0) {
            return new ArrayList<>();
        }

        //Instant date = Instant.now();
        LocalDateTime date = LocalDateTime.now();
        long time = 0;
        ChronoField chronoField = ChronoField.YEAR;
        double total = 0;
        double correct = 0;

        if (interval.equals("Days")) {
            time = date.getLong(ChronoField.EPOCH_DAY);
            chronoField = ChronoField.EPOCH_DAY;
        } else if (interval.equals("Weeks")) {
            time = date.getLong(ChronoField.EPOCH_DAY);
            chronoField = ChronoField.EPOCH_DAY;
            count = (count * 7);
        } else if (interval.equals("Months")) {
            time = date.getLong(ChronoField.PROLEPTIC_MONTH);
            chronoField = ChronoField.PROLEPTIC_MONTH;
        } else if (interval.equals("Years")) {
            time = date.getLong(ChronoField.YEAR);
            chronoField = ChronoField.YEAR;
        }

        for (Submission sub : submissions) {
            if ((time - count) <= sub.getCreationTime().getLong(chronoField)) {
                if (sub.getVerdict().equals("OK") || sub.getVerdict().equals("Accepted")) {
                    correct++;
                }
                total++;
            }
        }

        List<Double> stats = new ArrayList<>();
        stats.add(correct);
        stats.add(total);

        return stats;
    }

}
