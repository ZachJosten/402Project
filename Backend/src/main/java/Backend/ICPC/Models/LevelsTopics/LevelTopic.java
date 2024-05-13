package Backend.ICPC.Models.LevelsTopics;

import Backend.ICPC.Models.Levels.LevelKey;
import Backend.ICPC.Models.Levels.Levels;
import Backend.ICPC.Models.Topics.Topic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class LevelTopic {

    @ApiModelProperty(notes = "Composite Key for LevelTopic", example="{\"lid\":1,\"tid\":1}", required=true)
    @EmbeddedId
    private LevelTopicKey id = new LevelTopicKey();

    @ApiModelProperty(notes = "Associated Level", example = "{\"id\":1,\"desc\":\"First level\"}", required = true)
    @ManyToOne
    @MapsId("levelId")
    @JoinColumns({
        @JoinColumn(name="levels_level_number", referencedColumnName="level_number"),
        @JoinColumn(name="levels_school_id", referencedColumnName="school_id")
    })
    @JsonIgnore
    private Levels levels;

    @ApiModelProperty(notes = "Associated Topic", example = "{\"id\":1,\"name\":\"Data Structures\"}", required = true)
    @ManyToOne
    @MapsId("tid")
    @JoinColumn(name="topic_id")
    @JsonIgnore
    private Topic top;

    public LevelTopic()
    {

    }

    public LevelTopic(Levels levels, Topic topic)
    {
        LevelKey levelKey = new LevelKey(levels.getId().getLevelNum(), levels.getId().getSchoolId());
        id.setLevelId(levelKey);
        id.setTid(topic.getId());
        this.levels = levels;
        this.top = topic;
    }

    public LevelTopicKey getId() {
        return id;
    }

    @JsonIgnore
    public Levels getLevel() {
        return levels;
    }

    public void setLevel(Levels levels) {
        this.levels = levels;
    }

    @JsonIgnore
    public Topic getTopic() {
        return top;
    }

    public void setTopic(Topic topic) {
        this.top = topic;
    }
}
