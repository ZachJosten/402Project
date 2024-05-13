package Backend.ICPC.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import Backend.ICPC.Models.Levels.Levels;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("student")
public class Student extends User {
    @ApiModelProperty(notes = "Student ID", example = "1", required = true)
    private int sid; // distinct from user_id

    @ApiModelProperty(notes = "Team ID", example = "[{\"teamId\":1,\"coach\":{\"id\":1,\"name\":\"John Doe\",\"emailAddress\":\"johndoe@gmail.com\"},\"students\":[{\"id\":3,\"name\":\"Jack\",\"emailAddress\":\"jack@gmail.com\",\"sid\":352},{\"id\":4,\"name\":\"Jill\",\"emailAddress\":\"jill@gmail.com\",\"sid\":621}]}]", required = false)
    @ManyToOne
    @JoinColumn(name = "tid", referencedColumnName = "teamId", nullable = true, foreignKey = @ForeignKey(name = "fk_team_id", foreignKeyDefinition = "FOREIGN KEY (tid) REFERENCES Team(team_id) ON DELETE SET NULL"))
    @JsonIgnore
    private Team team;

    @ApiModelProperty(notes = "Relation to level", required = false)
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumns({
        @JoinColumn(name="lev_level_number", referencedColumnName="level_number"),
        @JoinColumn(name="lev_school_id", referencedColumnName="school_id")
    })
    @JsonIgnore
    private Levels lev;

    @ApiModelProperty(notes = "Submissions of the student", example = "[{\"id\":1,\"submissionId\":157298399,\"verdict\":\"OK\",\"subLink\":\"https://codeforces.com/problemset/submission/1677/157298399\"},{\"id\":2,\"submissionId\":157297949,\"verdict\":\"TIME_LIMIT_EXCEEDED\",\"subLink\":\"https://codeforces.com/problemset/submission/1677/157297949\"}]", required = false)
    @OneToMany(mappedBy = "subStudent", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Submission> submissionList;

    public Student()
    {
        super();
        team = null;
        lev = null;
        submissionList = new ArrayList<>();
    }

    // This constructor should be used in practice (requires Team, School)
    public Student(String name, String emailAddress, String password, int student_id, Team team, School school) {
        super(name, emailAddress, password, school);
        sid = student_id;
        this.team = team;
        lev = null;
        submissionList = new ArrayList<>();
    }

    public int getSid() {
        return this.sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @JsonIgnore
    public Team getTeam() {
        return this.team;
    }

    public Levels getLev() {
        return lev;
    }

    public void setLev(Levels lev) {
        this.lev = lev;
    }
    
    @JsonIgnore
    public List<Submission> getSubmissionList() {
        return submissionList;
    }

    public void addSubmissionList(Submission submission) {
        this.submissionList.add(submission);
    }

    public void deleteSubmissionList(Submission submission) {
        this.submissionList.remove(submission);
    }
}
