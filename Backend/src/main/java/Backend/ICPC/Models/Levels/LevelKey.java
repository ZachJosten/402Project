package Backend.ICPC.Models.Levels;

import Backend.ICPC.Models.TopicsProblems.TopicProblemKey;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LevelKey implements Serializable {

    @Column(name = "level_number")
    private int levelNum;  // Level number

    @Column(name = "school_id", nullable=true)
    private int schoolId;

    public LevelKey()
    {

    }

    public LevelKey(int level_num, int school_id)
    {
        this.levelNum = level_num;
        this.schoolId = school_id;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int level_num) {
        this.levelNum = level_num;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int school_id) {
        this.schoolId = school_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LevelKey that = (LevelKey) o;
        return levelNum == that.levelNum && schoolId == that.schoolId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(levelNum, schoolId);
    }
}
