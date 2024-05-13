package Backend.ICPC.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Submission {

    @ApiModelProperty(notes = "Database ID for submission", example = "1", required = true)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private int id;

    @ApiModelProperty(notes = "Problem associated with submission", example = "{\"id\":1,\"name\":\"Nene and the Passing Game\",\"problemLink\":\"https://codeforces.com/problemset/problem/1956/F\",\"conId\":1956,\"ind\":\"F\"}", required = true)
    @ManyToOne
    @JoinColumn(name = "sub_pid", nullable = false)
    @JsonIgnore
    private Problem subProb;

    @ApiModelProperty(notes = "Student associated with submission", example = "{\"id\":3,\"name\":\"Jack\",\"emailAddress\":\"jack@gmail.com\",\"sid\":352}", required = true)
    @ManyToOne
    @JoinColumn(name = "sub_sid", nullable = false)
    @JsonIgnore
    private Student subStudent;

    @ApiModelProperty(notes = "ID of submission on website", example = "5251911", required = true)
    private int submissionId;

    @ApiModelProperty(notes = "Verdict of submission", example = "Accepted", required = true)
    private String verdict;

    @ApiModelProperty(notes = "Link to submission(if exists)", example = "https://codeforces.com/problemset/submission/1677/157298399", required = false)
    private String subLink;

    @ApiModelProperty(notes = "Time submission was created", example = "to-do", required = true)
    private LocalDateTime creationTime;

    //More can go here, but remember to update the submission builder!

    public Submission()
    {

    }

    public Submission(Problem subProb, Student subStudent, int submissionId, String verdict)
    {
        this.subProb = subProb;
        this.subStudent = subStudent;
        this.submissionId = submissionId;
        this.verdict = verdict;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
    public Problem getSubProb() {
        return subProb;
    }

    public void setSubProb(Problem subProb) {
        this.subProb = subProb;
    }

    @JsonIgnore
    public Student getSubStudent() {
        return subStudent;
    }

    public void setSubStudent(Student subStudent) {
        this.subStudent = subStudent;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public String getVerdict() {
        return verdict;
    }

    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

    public String getSubLink() {
        return subLink;
    }

    public void setSubLink(String subLink) {
        this.subLink = subLink;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }
}
