package Backend.ICPC.Models.Levels;

import Backend.ICPC.Models.Levels.*;
import Backend.ICPC.Models.LevelsTopics.LevelTopic;
import Backend.ICPC.Models.Student;
import Backend.ICPC.Models.School;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Levels {

    @ApiModelProperty(notes = "Composite Key for Level", example="{\"lid\":1,\"school_id\":1}", required=false)
    @EmbeddedId
    private LevelKey id = new LevelKey();

    @ApiModelProperty(notes = "Description of level", example = "Beginner", required = true)
    private String descrip;

    @ApiModelProperty(notes = "Relation to topics", example ="[{\"id\":{\"lid\":1,\"tid\":1},\"topic\":{\"id\":1,\"name\":\"Data Structures\"}}]", required = false)
    @OneToMany(mappedBy="levels", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<LevelTopic> levelTopics;

    @ApiModelProperty(notes = "Relation to students", example = "[{\"id\":3,\"name\":\"Jack\",\"emailAddress\":\"jack@gmail.com\",\"sid\":352}]", required = false)
    @OneToMany(mappedBy="lev", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Student> students;

    @ApiModelProperty(notes="Relation to School", required = false)
    @ManyToOne
    @MapsId("schoolId")
    @JoinColumn(name = "school_id")
    @JsonIgnore
    private School school;

    public Levels()
    {
        levelTopics = new ArrayList<>();
        students = new ArrayList<>();
    }

    public Levels(int level_num, String descrip, School school)
    {
        id.setLevelNum(level_num);
        id.setSchoolId(school.getId());
        this.descrip = descrip;
        this.school = school;
        levelTopics = new ArrayList<>();
        students = new ArrayList<>();
    }

    public LevelKey getId() {
        return id;
    }

    public void setId(LevelKey id) {
        this.id = id;
    }

    public String getDesc() {
        return descrip;
    }

    public void setDesc(String descrip) {
        this.descrip = descrip;
    }

    @JsonIgnore
    public List<LevelTopic> getLevelTopics() {
        return levelTopics;
    }

    public void addLevelTopics(LevelTopic lt) {
        this.levelTopics.add(lt);
    }

    public void removeLevelTopics(LevelTopic lt){
        this.levelTopics.remove(lt);
    }

    @JsonIgnore
    public List<Student> getStudents() {
        return students;
    }

    public void addStudents(Student student) {
        this.students.add(student);
    }

    public void removeStudents(Student student) {
        this.students.remove(student);
    }
}
