package Backend.ICPC.Controllers;

import Backend.ICPC.APIs.URLBuilder;
import Backend.ICPC.Models.Problem;
import Backend.ICPC.Models.WebsiteSpecific.Codeforces.CodeforcesProblem;
import Backend.ICPC.Models.WebsiteSpecific.Kattis.KattisProblem;
import Backend.ICPC.Models.WebsiteSpecific.uHunt.uHuntProblem;
import Backend.ICPC.Repositories.ProblemRepository;
import Backend.ICPC.Repositories.WebsiteSpecific.Codeforces.CFProblemRepository;
import Backend.ICPC.Repositories.WebsiteSpecific.Kattis.KattisRepository;
import Backend.ICPC.Repositories.WebsiteSpecific.uHunt.uHuntProblemRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.aspectj.apache.bcel.classfile.Code;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Api(value = "Problem Controller", description="Controller for controlling problem related info", tags="ProblemController")
@RestController
public class ProblemController {

    private String[] websites = {"Codeforces", "uHunt"};

    private ObjectMapper mapper = new ObjectMapper();

    private URLBuilder urlBuilder = new URLBuilder();

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    CFProblemRepository cfProblemRepository;

    @Autowired
    uHuntProblemRepository uHuntProblemRepository;

    @Autowired
    KattisRepository kattisRepository;

    @ApiOperation(value = "Get a list of all problems", response=Problem.class, tags="getAllProblems")
    @GetMapping(path = {"/problems"})
    List<Problem> getAllProblems() { return this.problemRepository.findAll(); }

    @ApiOperation(value = "Get a problem by it's ID", response = Problem.class, tags="getProblemById")
    @GetMapping(path="/problems/{pid}")
    Problem getProblemById(@PathVariable int pid) { return this.problemRepository.findById(pid); }

    @ApiOperation(value = "Get a list of all Codeforces problems", response=CodeforcesProblem.class, tags="getAllCFProblems")
    @GetMapping(path = {"/cfproblems"})
    List<CodeforcesProblem> getAllCFProblems() { return this.cfProblemRepository.findAll(); }

    @ApiOperation(value = "Get a list of all uHunt problems", response=uHuntProblem.class, tags="getAllUHProblems")
    @GetMapping(path = {"/uhproblems"})
    List<uHuntProblem> getAllUHProblems() {return this.uHuntProblemRepository.findAll(); }

    @ApiOperation(value = "Get a list of all Kattis problems", response = KattisProblem.class, tags = "getAllKattisProblems")
    @GetMapping(path = {"/katproblems"})
    List<KattisProblem> getAllKattisProblems() { return this.kattisRepository.findAll(); }

    @ApiOperation(value = "Update the entire database of problems")
    @Scheduled(cron = "0 0 */3 * * *")
    //@Scheduled(cron = "0 * * * * *")
    void updateProblemsDatabase() throws MalformedURLException, IOException
    {
        urlBuilder.setTask("Problems");
        //Iterate through each website we have on the list.
        for(String web : websites)
        {
            urlBuilder.setWebsite(web);
            String yourl = urlBuilder.buildURL();
            JsonNode resu = mapper.readTree(new URL(yourl));
            Iterator<JsonNode> tempIt;
            String probLink = "";
            //Begin cases for each website.
            //Unfortunately is messy due to each website being unique in terms of fields.
            if(web.equals("Codeforces")) {
                tempIt = resu.get("result").get("problems").iterator();
                while(tempIt.hasNext())
                {
                    JsonNode temp = tempIt.next();
                    CodeforcesProblem cf = new CodeforcesProblem();
                    cf.setInd(temp.get("index").asText());
                    cf.setName(temp.get("name").asText());
                    cf.setConId(temp.get("contestId").asInt());
                    probLink = "https://codeforces.com/problemset/problem/" + cf.getConId() + "/" + cf.getInd();
                    cf.setProblemLink(probLink);
                    /*
                    Iterator<JsonNode> tagIt = temp.get("tags").iterator();
                    while(tagIt.hasNext())
                    {
                        cf.setTags(tagIt.next().asText());
                    }
                    */
                    List<CodeforcesProblem> check = cfProblemRepository.findCurrentExisting(cf.getConId(), cf.getInd());
                    if(check.isEmpty())
                    {
                        this.cfProblemRepository.save(cf);
                    }
                }
            }
            else if(web.equals("uHunt"))
            {
                tempIt = resu.iterator();
                while(tempIt.hasNext())
                {
                    JsonNode temp = tempIt.next();
                    uHuntProblem uh = new uHuntProblem();
                    uh.setUid(temp.get(0).asInt());
                    uh.setNumP(temp.get(1).asInt());
                    uh.setName(temp.get(2).asText());
                    int volume = (int)(uh.getNumP() / 100);
                    probLink = "https://onlinejudge.org/external/" + volume + "/" + uh.getNumP() + ".pdf";
                    uh.setProblemLink(probLink);
                    List<uHuntProblem> check = uHuntProblemRepository.findCurrentExisting(uh.getUid());
                    if(check.isEmpty())
                    {
                        this.uHuntProblemRepository.save(uh);
                    }
                }
            }
            else if(web.equals("Kattis"))
            {
                tempIt = resu.get("problems").iterator();
                while(tempIt.hasNext())
                {
                    JsonNode temp = tempIt.next();
                    KattisProblem kp = new KattisProblem();
                    kp.setKatId(temp.get("id").asInt());
                    kp.setName(temp.get("name").asText());
                    //Set problem link
                    List<KattisProblem> check = kattisRepository.findCurrentExisting(kp.getKatId());
                    if(check.isEmpty())
                    {
                        this.kattisRepository.save(kp);
                    }
                }
            }
        }
    }

    @ApiOperation(value = "Grab Problems by filters", response=JSONArray.class, tags="grabProblems")
    @GetMapping(path = {"/grabprob/{pNum}"})
    JSONArray grabProblems(@RequestBody String test, @PathVariable int pNum) throws MalformedURLException, IOException
    {
        JSONObject in = new JSONObject(test);
        JSONArray job = new JSONArray();
        String website = in.get("website").toString();
        List<Problem> websiteProbs;
        if(!website.isEmpty()) {
            websiteProbs = problemRepository.findByOrigin(website);
        }
        else
        {
            websiteProbs = problemRepository.findAll();
        }


        for(int i = (pNum-1)*20; i < (pNum*20); i++) {
            JSONObject entry;
            Problem prob = websiteProbs.get(i);
            //I am worried that doing it this way exposes passwords and such.
            if(website.equals("Codeforces"))
            {
                entry = new JSONObject(cfProblemRepository.findById(prob.getId()));
            }
            else if(website.equals("uHunt"))
            {
                entry = new JSONObject(uHuntProblemRepository.findById(prob.getId()));
            }
            else
            {
                entry = new JSONObject(prob);
            }
            job.put(entry);
        }
        return job;
    }

}
