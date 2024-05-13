package Backend.ICPC.Models.LevelsTopics;

import Backend.ICPC.Models.Levels.LevelKey;
import Backend.ICPC.Models.TopicsProblems.TopicProblemKey;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LevelTopicKey implements Serializable {

    // @Column(name = "level_id") // No columns specification for composite key
    private LevelKey levelId;

    @Column(name = "topic_id")
    private int tid;

    public LevelTopicKey()
    {

    }

    public LevelTopicKey(LevelKey levelKey, int tid)
    {
        this.levelId = levelKey;
        this.tid = tid;
    }

    public LevelKey getLevelId() {
        return levelId;
    }

    public void setLevelId(LevelKey levelKey) {
        this.levelId = levelKey;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LevelTopicKey that = (LevelTopicKey) o;
        return levelId.equals(that.levelId) && tid == that.tid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(levelId, tid);
    }
}
