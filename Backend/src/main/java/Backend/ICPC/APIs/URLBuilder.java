package Backend.ICPC.APIs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

/*
This is a class that serves for use alongside JsonNode ObjectMapping, specifically for use of URL based API calls.
The purpose of the class is mainly to reduce duplicate code and also making it simpler in design/easier to read and use.

PLEASE NOTE - If a website is to be added to the general application's database, it is required that there be implementation
in this class AND in the ProblemController and SubmissionController. The two controllers hold builders that mainly deal
with the semantics of each particular websites returning of API calls, as there is inconsistency between each website's APIs.
A websites URLs for API calls must be added here, specifically for definition of where the builders should be calling to get
a response. Generally code can be reused for the builders and links, but obviously require manual inputs on the specifics
of each website.
 */
public class URLBuilder {
    //Website we are dealing with
    private String website;

    //What should be carried out with the API
    private String task;

    //Additional information required for API call
    private String target;

    //String to build on and return at end of call
    private String base;

    public URLBuilder()
    {
        website = "";
        task = "";
        target = "";
        base = "";
    }

    public URLBuilder(String website)
    {
        this.website = website;
        this.task = "";
        this.target = "";
        this.base = "";
    }

    //Split between each website. Cleaner than without helper methods.
    public String buildURL()
    {
        if(website.equals("Codeforces"))
        {
            buildCodeforces();
        }
        if(website.equals("uHunt"))
        {
            buildUHunt();
        }
        if(website.equals("Kattis"))
        {
            buildKattis();
        }

        return base;
    }

    //Codeforces specific stuff. Link building is different depending on task.
    private void buildCodeforces()
    {
        base = "https://codeforces.com/";
        if(task.equals("Problems"))
        {
            base = base + "api/problemset.problems";
        }
        else if(task.equals("Submissions"))
        {
            base = base + "api/user.status?handle=" + target;
        }
        else if(task.equals("Submission Link"))
        {
            base = base + "problemset/submission/" + target;
        }
    }

    //uHunt specific stuff. Link building is different depending on task.
    private void buildUHunt()
    {
        base = "https://uhunt.onlinejudge.org/";
        if(task.equals("Problems"))
        {
            base = base + "api/p";
        }
        else if(task.equals("Submissions"))
        {
            base = base + "api/subs-user/" + target;
        }
        else if(task.equals("Convert"))
        {
            base = base + "api/uname2uid/" + target;
        }
    }

    //Kattis specific stuff. Link building is different depending on task.
    private void buildKattis()
    {
        base = "https://iastate.kattis.com/";
        if(task.equals("Problems"))
        {
            base = base + "problems";
        }
        else if(task.equals("Submissions"))
        {
            base = base + "user/" + target + "/submissions";
        }
        else if(task.equals("Submission Link"))
        {
            base = base;
        }
    }

    //Setters for various required information.

    public void setWebsite(String website)
    {
        this.website = website;
    }

    public void setTask(String task) { this.task = task; }

    public void setTarget(String target) { this.target = target; }
}
