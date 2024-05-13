package Backend.ICPC.Models;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Team {

    @ApiModelProperty(notes = "Team unique identifier", example = "1", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teamId;

    @ApiModelProperty(notes = "Coach ID", example = "1", required = false)
    @ManyToOne
    @JoinColumn(name = "cid", 
                referencedColumnName = "id",
                nullable = true, 
                foreignKey = @ForeignKey(
                    name = "fk_coach_id", 
                    value = ConstraintMode.CONSTRAINT,
                    foreignKeyDefinition = "FOREIGN KEY (cid) REFERENCES users(id) ON DELETE SET NULL")) 
    @JsonIgnore
    private Coach coach;

    @ApiModelProperty(notes = "Student IDs", example = "{\"id\":3,\"name\":\"Jack\",\"emailAddress\":\"jack@gmail.com\",\"sid\":352}", required = true)
    @OneToMany(mappedBy = "team")
    @JsonIgnore
    private List<Student> students;

    // @ApiModelProperty(notes="Teams School", required = false)
    // @ManyToOne
    // @JoinColumn(name = "school_id")
    // @JsonIgnore
    // private School school;

    public Team() {
        students = new ArrayList<>();
    }

    public Team(Coach coach) {
        this.coach = coach;
        students = new ArrayList<>();
    }

    public Team(Coach coach, List<Student> students) {
        this.coach = coach;
        
        this.students = new ArrayList<>();
        for (Student s : students){
            this.students.add(s);
        }
    }

    public int getTeamId() {
        return teamId;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }
}
