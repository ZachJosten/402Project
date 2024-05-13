package Backend.ICPC.Controllers;

import Backend.ICPC.APIs.URLBuilder;
import Backend.ICPC.Models.Student;
import Backend.ICPC.Models.Submission;
import Backend.ICPC.Models.WebsiteSpecific.Codeforces.CodeforcesProblem;
import Backend.ICPC.Models.WebsiteSpecific.Kattis.KattisProblem;
import Backend.ICPC.Models.WebsiteSpecific.uHunt.uHuntProblem;
import Backend.ICPC.Repositories.ProblemRepository;
import Backend.ICPC.Repositories.StudentRepository;
import Backend.ICPC.Repositories.SubmissionRepository;
import Backend.ICPC.Repositories.WebsiteSpecific.Codeforces.CFProblemRepository;
import Backend.ICPC.Repositories.WebsiteSpecific.Kattis.KattisRepository;
import Backend.ICPC.Repositories.WebsiteSpecific.uHunt.uHuntProblemRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Api(value = "Submission controller", description="REST API related to Submission Entity", tags="SubmissionController")
@RestController
public class SubmissionController {

    private String[] websites = {"Codeforces", "uHunt"};

    private ObjectMapper mapper = new ObjectMapper();

    private URLBuilder urlBuilder = new URLBuilder();

    @Autowired
    SubmissionRepository submissionRepository;

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    CFProblemRepository cfProblemRepository;

    @Autowired
    uHuntProblemRepository uHuntProblemRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    KattisRepository kattisRepository;

    @ApiOperation(value = "Get a submission by id", response = Submission.class, tags = "getSubmissionById")
    @GetMapping(path = {"/submissions/{sid}"})
    Submission getSubmissionById(@PathVariable int sid)
    {
        return this.submissionRepository.findById(sid);
    }

    @ApiOperation(value = "Update submissions")
    @Scheduled(cron = "0 */30 * * * *")
    //@Scheduled(cron = "0 * * * * *")
    void updateSubmissions() throws MalformedURLException, IOException
    {
        /*
        Likely update to be implemented is only grabbing submissions of assigned problems, just for consistency between assigned and submitted.
         */

        //Make url builder do correct job.
        urlBuilder.setTask("Submissions");
        //For each website from our website list defined above
        for(String web : websites)
        {
            //Some slight setup for later tools we use.
            urlBuilder.setWebsite(web);
            String yourl;
            JsonNode resu;
            Iterator<JsonNode> tempIt;
            List<Student> students = studentRepository.findAll();

            //For all students in our database
            for(Student student : students)
            {
                //Cases between different websites, unfortunately is
                //very ugly, but due to inconsistencies between each
                //website's API, is required.
                if(web.equals("Codeforces") && student.getAccountInfo() != null)
                {
                    if(!student.getAccountInfo().getCfHandle().isEmpty()) {
                        //Get url builder targets set correctly
                        urlBuilder.setTarget(student.getAccountInfo().getCfHandle());

                        //Get tools for iterating through submissions ready
                        yourl = urlBuilder.buildURL();
                        resu = mapper.readTree(new URL(yourl));
                        tempIt = resu.get("result").iterator();

                        //For each submission we have
                        while (tempIt.hasNext()) {
                            //Ugly but basically just throws submission information into correct form for database.
                            JsonNode temp = tempIt.next();
                            Submission submission = new Submission();
                            submission.setSubmissionId(temp.get("id").asInt());
                            submission.setSubStudent(student);
                            LocalDateTime date = LocalDateTime.ofEpochSecond(temp.get("creationTimeSeconds").asLong(), 0, ZoneOffset.of("-06:00"));
                            submission.setCreationTime(date);
                            //System.out.println("ConID: " + cfProb.getConId() + " | Index: " + cfProb.getInd());
                            List<CodeforcesProblem> cf = cfProblemRepository.findCurrentExisting(temp.get("contestId").asInt(), temp.get("problem").get("index").asText());
                            if(cf.size() != 0) {
                                submission.setSubProb(cf.get(0));
                                submission.setVerdict(temp.get("verdict").asText());
                                //We don't want duplicates, so we check if we already have this submission.
                                if (submissionRepository.findCurrentExisting(cf.get(0).getId(), student.getId(), submission.getSubmissionId()).isEmpty()) {
                                    this.submissionRepository.save(submission);
                                }
                            }
                        }
                    }
                }
                else if(web.equals("uHunt") && student.getAccountInfo() != null)
                {
                    if(!student.getAccountInfo().getuHandle().isEmpty()) {
                        //Unlike codeforces, we need to convert the handle to an id first.
                        urlBuilder.setTask("Convert");
                        urlBuilder.setTarget(student.getAccountInfo().getuHandle());
                        String uid = mapper.readTree(new URL(urlBuilder.buildURL())).asText();
                        urlBuilder.setTask("Submissions");
                        urlBuilder.setTarget(uid);

                        //Setup tools for reading submissions
                        yourl = urlBuilder.buildURL();
                        resu = mapper.readTree(new URL(yourl));
                        tempIt = resu.get("subs").iterator();

                        //For each submission we have.
                        while (tempIt.hasNext()) {
                            //Like codeforces we have an ugly bit of code,
                            //but all it does it format stuff into the database.
                            JsonNode temp = tempIt.next();
                            Submission submission = new Submission();
                            List<uHuntProblem> uh = this.uHuntProblemRepository.findCurrentExisting(temp.get(1).asInt());
                            if(uh.size() != 0) {
                                submission.setSubProb(uh.get(0));
                                submission.setSubStudent(student);
                                submission.setSubmissionId(temp.get(0).asInt());
                                LocalDateTime date = LocalDateTime.ofEpochSecond(temp.get(4).asLong(), 0, ZoneOffset.of("-06:00"));
                                submission.setCreationTime(date);

                                //There are verdict codes on uHunt, we are just giving them the correct translations.
                                int verdId = temp.get(2).asInt();
                                String verdict;
                                if (verdId == 10) {
                                    verdict = "Submission error";
                                } else if (verdId == 15) {
                                    verdict = "Can't be judged";
                                } else if (verdId == 20) {
                                    verdict = "In queue";
                                } else if (verdId == 30) {
                                    verdict = "Compile error";
                                } else if (verdId == 35) {
                                    verdict = "Restricted function";
                                } else if (verdId == 40) {
                                    verdict = "Runtime error";
                                } else if (verdId == 45) {
                                    verdict = "Output limit";
                                } else if (verdId == 50) {
                                    verdict = "Time limit";
                                } else if (verdId == 60) {
                                    verdict = "Memory limit";
                                } else if (verdId == 70) {
                                    verdict = "Wrong answer";
                                } else if (verdId == 80) {
                                    verdict = "PresentationE";
                                } else if (verdId == 90) {
                                    verdict = "Accepted";
                                } else {
                                    verdict = "Unknown verdict";
                                }

                                submission.setVerdict(verdict);

                                //We don't want duplicates, check if we already have it. If not, then add it.
                                if (submissionRepository.findCurrentExisting(uh.get(0).getId(), student.getId(), submission.getSubmissionId()).isEmpty()) {
                                    this.submissionRepository.save(submission);
                                }
                            }
                        }
                    }


                }
                else if(web.equals("Kattis") && student.getAccountInfo() != null)
                {
                    if(!student.getAccountInfo().getkHandle().isEmpty())
                    {
                        urlBuilder.setTarget(student.getAccountInfo().getkHandle());

                        yourl = urlBuilder.buildURL();
                        resu = mapper.readTree(new URL(yourl));
                        Iterator<JsonNode> tempItProbs = resu.get("problems").iterator();

                        while(tempItProbs.hasNext())
                        {
                            JsonNode tempProb = tempItProbs.next();
                            tempIt = tempProb.get("submissions").iterator();
                            while (tempIt.hasNext()) {
                                JsonNode temp = tempIt.next();
                                Submission submission = new Submission();
                                List<KattisProblem> kProb = this.kattisRepository.findCurrentExisting(tempProb.get("id").asInt());

                                if(kProb.size() != 0)
                                {
                                    submission.setSubmissionId(temp.get("number").asInt());
                                    submission.setSubProb(kProb.get(0));
                                    submission.setSubStudent(student);
                                    submission.setVerdict(temp.get("result").asText());
                                    if (submissionRepository.findCurrentExisting(kProb.get(0).getId(), student.getId(), submission.getSubmissionId()).isEmpty()) {
                                        this.submissionRepository.save(submission);
                                    }
                                }


                            }
                        }

                    }

                }
            }

        }

        //uHunt does not have a page for each submission, so we only look at Codeforces.
        //TODO: IF ANOTHER WEBSITE IS ADDED, NEED TO REWORK THIS.
        urlBuilder.setWebsite("Codeforces");
        urlBuilder.setTask("Submission Link");
        List<Submission> subs = submissionRepository.findAll();
        //For all of our submissions
        for(Submission sub : subs)
        {
            //If it doesn't exist in the Codeforces repository, we don't care about it.
            CodeforcesProblem cprob = this.cfProblemRepository.findById(sub.getSubProb().getId());
            if(cprob != null)
            {
                //Give the submission a submission link.
                urlBuilder.setTarget("" + cprob.getConId() + "/" + sub.getSubmissionId());
                sub.setSubLink(urlBuilder.buildURL());
                submissionRepository.save(sub);
            }
        }

    }
}
