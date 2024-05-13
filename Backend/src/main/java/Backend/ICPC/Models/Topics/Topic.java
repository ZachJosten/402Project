package Backend.ICPC.Models.Topics;

import Backend.ICPC.Models.School;
import Backend.ICPC.Models.LevelsTopics.LevelTopic;
import Backend.ICPC.Models.TopicsProblems.TopicProblem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Topic {

    @ApiModelProperty(notes = "ID of Topic", example = "1", required = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id; // This remains the simple generated ID

    @ApiModelProperty(notes = "Name of topic", example = "Data Structures", required = true)
    private String name;

    @ApiModelProperty(notes = "Relation to problem", example = "[{\"id\":{\"tid\":1,\"pid\":1}}]", required = false)
    @OneToMany(mappedBy="topic", cascade=CascadeType.REMOVE)
    @JsonIgnore
    private List<TopicProblem> topicProblems;

    @ApiModelProperty(notes = "Relation to level", example = "[{\"id\":{\"lid\":1,\"tid\":1},\"topic\":{\"id\":1,\"name\":\"Data Structures\"}}]", required = false)
    @OneToMany(mappedBy = "top", cascade=CascadeType.REMOVE)
    @JsonIgnore
    private List<LevelTopic> levelTopics;

    @ApiModelProperty(notes = "School of Topic", required = false)
    @ManyToOne
    @JoinColumn(name = "school_id", nullable = true)
    @JsonIgnore
    private School school;

    public Topic()
    {
        topicProblems = new ArrayList<>();
        levelTopics = new ArrayList<>();
    }

    public Topic(String name, School school)
    {
        this.name = name;
        this.school = school;
        topicProblems = new ArrayList<>();
        levelTopics = new ArrayList<>();
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

    @JsonIgnore
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

    @JsonIgnore
    public List<LevelTopic> getLevelTopics() { return levelTopics; }

    public void addLevelTopics(LevelTopic lt) { this.levelTopics.add(lt); }

    public void removeLevelTopics(LevelTopic lt) { this.levelTopics.remove(lt); }

    @JsonIgnore
    public School getSchool() {
        return this.school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
