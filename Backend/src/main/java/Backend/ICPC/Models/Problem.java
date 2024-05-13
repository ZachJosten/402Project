package Backend.ICPC.Models;

import Backend.ICPC.Models.TopicsProblems.TopicProblem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name="problems")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="webOri", discriminatorType = DiscriminatorType.STRING)
public class Problem {

    @ApiModelProperty(notes = "Problem ID(for this team)", example = "1", required = true)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private int id;

    @ApiModelProperty(notes = "Name of problem", example = "3N+1", required = true)
    private String name;

    @ApiModelProperty(notes = "Relation to topics", example = "to-do", required = false)
    @OneToMany(mappedBy="problem", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<TopicProblem> topicProblems;

    @ApiModelProperty(notes = "List of submissions attached to problem", example = "to-do", required = false)
    @OneToMany(mappedBy = "subProb", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Submission> submissionList;


    //@ApiModelProperty(notes = "Associated ")
    //TODO: Before this implementation, should probably have tag objects, will play much nicer with database.
    //private List<String> tags;

    @ApiModelProperty(notes = "Link leading to the problem", example = "https://codeforces.com/problemset/problem/1952/I", required=false)
    private String problemLink;

    public Problem()
    {
        topicProblems = new ArrayList<>();
        submissionList = new ArrayList<>();
    }

    public Problem(String name)
    {
        this.name = name;
        this.topicProblems = new ArrayList<>();
        submissionList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TopicProblem> getTopicProblems() {
        return topicProblems;
    }

    public void addTopicProblems(TopicProblem tp) {
        this.topicProblems.add(tp);
    }

    public void removeTopicProblems(TopicProblem tp)
    {
        this.topicProblems.remove(tp);
    }

    /*
    public List<String> getTags() {
        return tags;
    }

    public void setTags(String tag) {
        this.tags.add(tag);
    }
    */

    public String getProblemLink() {
        return problemLink;
    }

    public void setProblemLink(String problemLink) {
        this.problemLink = problemLink;
    }

    public List<Submission> getSubmissionList() {
        return submissionList;
    }

    public void addSubmissionList(Submission submission) {
        this.submissionList.add(submission);
    }

    public void removeSubmissionList(Submission submission){
        this.submissionList.remove(submission);
    }
}
