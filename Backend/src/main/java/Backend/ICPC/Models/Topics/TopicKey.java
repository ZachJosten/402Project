package Backend.ICPC.Models.Topics;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TopicKey implements Serializable {

    @Column(name = "topic_id")
    private int id;

    @Column(name = "school_id")
    private int schoolId;

    public TopicKey() {}

    public TopicKey(int id, int schoolId) {
        this.id = id;
        this.schoolId = schoolId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicKey topicKey = (TopicKey) o;
        return id == topicKey.id &&
               schoolId == topicKey.schoolId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, schoolId);
    }
}
