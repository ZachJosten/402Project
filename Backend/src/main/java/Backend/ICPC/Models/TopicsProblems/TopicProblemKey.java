package Backend.ICPC.Models.TopicsProblems;

import javax.persistence.*;

import Backend.ICPC.Models.Topics.TopicKey;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TopicProblemKey implements Serializable {

    @Column(name = "topic_id")
    private int tid;

    @Column(name = "problem_id")
    private int pid;

    public TopicProblemKey()
    {

    }

    public TopicProblemKey(int tid, int pid)
    {
        this.tid = tid;
        this.pid = pid;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicProblemKey that = (TopicProblemKey) o;
        return pid == that.pid && tid==that.tid;
    }


    @Override
    public int hashCode() {
        return Objects.hash(tid,pid);
    }
}
