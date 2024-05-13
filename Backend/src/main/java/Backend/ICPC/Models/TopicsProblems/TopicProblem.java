package Backend.ICPC.Models.TopicsProblems;

import Backend.ICPC.Models.Problem;
import Backend.ICPC.Models.Student;
import Backend.ICPC.Models.Team;
import Backend.ICPC.Models.Topics.Topic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class TopicProblem {

    @ApiModelProperty(notes = "Composite Key for TopicProblem", example="{\"tid\":1,\"pid\":1}", required=true)
    @EmbeddedId
    private TopicProblemKey id = new TopicProblemKey();

    @ApiModelProperty(notes = "Associated Topic", example = "[{\"id\":1,\"name\":\"Data Structures\"}]", required = true)
    @ManyToOne
    @MapsId("tid")
    @JoinColumn(name="topic_id")
    @JsonIgnore
    private Topic topic;

    @ApiModelProperty(notes = "Associated Problem", example="{ 'id': 1, 'name': '3N', 'conId': 1932, 'ind': 'E' }", required=true)
    @ManyToOne
    @MapsId("pid")
    @JoinColumn(name="problem_id")
    @JsonIgnore
    private Problem problem;

    public TopicProblem()
    {

    }

    public TopicProblem(Topic topic, Problem problem)
    {
        this.problem = problem;
        this.topic = topic;
    }

    public TopicProblemKey getId() {
        return id;
    }

    @JsonIgnore
    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    @JsonIgnore
    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
